package com.gnt.ecom.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${jwt.header-string:Authorization}")
    private String jwtHeader;

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(jwtHeader);
            final String tokenPrefix = "Bearer ";

            if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
                logger.warn("Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                String jwtToken = authHeader.substring(tokenPrefix.length());
                if (!jwtProvider.isTokenValid(jwtToken)) {
                    logger.warn("Invalid or expired JWT token");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String username = jwtProvider.extractUsername(jwtToken);
                List<String> roles = jwtProvider.extractRoles(jwtToken);

                exchange.getRequest().mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Roles", String.join(",", roles))
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .build();

                return chain.filter(exchange);
            } catch (Exception e) {
                logger.error("Error processing JWT token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.just(exchange.getResponse().setComplete()).block();
            }
        };
    }

    public static class Config {
        // Add configuration properties if needed (e.g., public routes to skip)
    }
}
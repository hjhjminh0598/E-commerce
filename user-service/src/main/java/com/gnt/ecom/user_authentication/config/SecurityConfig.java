package com.gnt.ecom.user_authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.user.service.UserService;
import com.gnt.ecom.user_authentication.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/auth/login/**",
                                "/api/v1/users/auth/refresh_token",
                                "/login/oauth2/code/google").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo -> userInfo
                                        .oidcUserService(oidcUserService())
                                )
                                .successHandler((request, response, authentication) -> {
//                            response.sendRedirect("/api/v1/users/auth/success");
                                    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                                    OAuth2Response oauth2Response = userService.loginWithGoogle(token);
                                    BaseResponse<OAuth2Response> baseResponse = BaseResponse.success(oauth2Response);
                                    response.setContentType("application/json");
                                    response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
                                })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
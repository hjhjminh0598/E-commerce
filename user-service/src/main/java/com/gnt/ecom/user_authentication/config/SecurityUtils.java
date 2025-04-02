package com.gnt.ecom.user_authentication.config;

import com.gnt.ecom.user.entity.User;
import com.gnt.ecom.user_authentication.entity.MyUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public final class SecurityUtils {

    public static Optional<Authentication> getCurrentAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication());
    }

    public static Optional<UserDetails> getCurrentUserDetails() {
        return getCurrentAuthentication()
                .map(Authentication::getPrincipal)
                .filter(UserDetails.class::isInstance)
                .map(UserDetails.class::cast);
    }

    public static Optional<MyUserDetails> getCurrentMyUserDetails() {
        return getCurrentAuthentication()
                .map(Authentication::getPrincipal)
                .filter(MyUserDetails.class::isInstance)
                .map(MyUserDetails.class::cast);
    }

    public static Optional<User> getCurrentUser() {
        return getCurrentMyUserDetails()
                .map(MyUserDetails::getUser);
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentMyUserDetails()
                .map(MyUserDetails::getUsername);
    }

    public static Optional<UUID> getCurrentUserId() {
        return getCurrentUser()
                .map(User::getId);
    }

    public static boolean isAuthenticated() {
        return getCurrentAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }
}
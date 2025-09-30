package com.school.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*@Component
public class CustomLoggingFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String username = "unknown";
//            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
//                jwtAuth.getToken().getClaims().forEach((k, v) -> log.info("Claim {} = {}", k, v));
//            } else {
//                username = authentication.getName();
//            }
//            log.info("[AUTHENTICATION] principal={} uri={}", username, request.getRequestURI());
//        } else {
//            log.warn("[AUTHENTICATION] unauthenticated request to {}", request.getRequestURI());
//
//        }
        if (authentication != null && authentication.isAuthenticated()) {
            String username;

            // Try to extract JWT claims regardless of wrapper type
            Object principal = authentication.getPrincipal();
            log.info("Principal: {}", principal.toString());
            log.info("Authentication Details: {}", authentication.getDetails().toString());
            log.info("Authentication Credentials: {}", authentication.getCredentials().toString());


            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                // Direct JWT access
                String preferredUsername = jwt.getClaimAsString("preferred_username");
                username = preferredUsername != null ? preferredUsername : jwt.getSubject();
            } else if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                // JwtAuthenticationToken wrapper
                String preferredUsername = jwtAuth.getToken().getClaimAsString("preferred_username");
                username = preferredUsername != null ? preferredUsername : jwtAuth.getName();
            } else {
                // fallback
                username = authentication.getName();
            }

            log.info("[AUTHENTICATION] principal={} uri={}", username, request.getRequestURI());
        } else {
            log.warn("[AUTHENTICATION] unauthenticated request to {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}*/
@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Only log authentication info for endpoints starting with /api
        if (path.startsWith("/api")) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String username = null;

                Object principal = authentication.getPrincipal();

                // OIDC (Keycloak) user
                if (principal instanceof OidcUser oidcUser) {
                    username = (String) oidcUser.getAttributes().getOrDefault("preferred_username", oidcUser.getName());
                    String rawIdToken = oidcUser.getIdToken().getTokenValue();
                    log.info("[AUTHENTICATION] raw ID token: {}", rawIdToken);

                }
                // JWT token authentication
                else if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                    username = jwtAuth.getToken().getClaimAsString("preferred_username");
                    log.info("[AUTHENTICATION] JWT claims: {}", jwtAuth.getToken().getClaims());
                }
                // Classic UserDetails (username/password)
                else if (principal instanceof org.springframework.security.core.userdetails.User user) {
                    username = user.getUsername();
                }
                // Fallback
                else {
                    username = authentication.getName();
                }

                log.info("[AUTHENTICATION] principal={} uri={}", username, path);
            } else {
                log.warn("[AUTHENTICATION] unauthenticated request to {}", path);
            }
        }

        // Proceed with the filter chain regardless of path
        filterChain.doFilter(request, response);
    }
}

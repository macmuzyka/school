package com.school.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/api")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username;

                Object principal = authentication.getPrincipal();
                if (principal instanceof OidcUser oidcUser) {
                    username = (String) oidcUser.getAttributes().getOrDefault("preferred_username", oidcUser.getName());
                    String rawIdToken = oidcUser.getIdToken().getTokenValue();
                    log.info("[AUTHENTICATION] raw ID token: {}", rawIdToken);

                } else if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                    username = jwtAuth.getToken().getClaimAsString("preferred_username");
                    log.info("[AUTHENTICATION] JWT claims: {}", jwtAuth.getToken().getClaims());
                } else {
                    username = authentication.getName();
                }
                log.info("[AUTHENTICATION] principal={} uri={}", username, path);
            } else {
                log.warn("[AUTHENTICATION] unauthenticated request to {}", path);
            }
        }

        filterChain.doFilter(request, response);
    }
}

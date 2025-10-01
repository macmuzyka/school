package com.school.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username;

            Object principal = authentication.getPrincipal();
            if (principal instanceof OidcUser oidcUser) {
                username = (String) oidcUser.getAttributes().getOrDefault("preferred_username", oidcUser.getName());
                String rawIdToken = oidcUser.getIdToken().getTokenValue();
                log.info("[AUTHENTICATION] raw ID token: {}", rawIdToken);
            } else {
                username = authentication.getName();
            }
            log.info("[AUTHENTICATION] principal={} uri={}", username, path);
        } else {
            log.warn("[AUTHENTICATION] unauthenticated request to {}", path);
        }

        log.info("---- Incoming Request Headers ----");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            log.info("{} = {}", name, value);
        }
        log.info("---- End Headers ----");

        filterChain.doFilter(request, response);
    }
}

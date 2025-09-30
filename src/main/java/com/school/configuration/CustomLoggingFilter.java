package com.school.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomLoggingFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username;
            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                username = jwtAuthenticationToken.getToken().getClaimAsString("preferred_username");
            } else {
                username = authentication.getName();
            }
            //TODO: display username, not user UUID
            log.info("[AUTHENTICATION] principal={} uri={}", username, request.getRequestURI());
        } else {
            log.warn("[AUTHENTICATION] unauthenticated request to {}", request.getRequestURI());

        }

        filterChain.doFilter(request, response);
    }
}

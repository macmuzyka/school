package com.school.configuration;

import com.school.service.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final CustomLoggingFilter customLoggingFilter;

    public SecurityConfig(RestAccessDeniedHandler restAccessDeniedHandler,
                          CustomLoggingFilter customLoggingFilter) {
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.customLoggingFilter = customLoggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (stateless)
                .csrf(csrf -> csrf.disable())
                // All /api/** endpoints require authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Exception handling
                .exceptionHandling(ex -> ex.accessDeniedHandler(restAccessDeniedHandler))
                // Resource server with JWT
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        // Custom logging filter after JWT validation
        http.addFilterAfter(customLoggingFilter, BasicAuthenticationFilter.class);

        return http.build();
    }
}

/*@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final CustomLoggingFilter customLoggingFilter;

    public SecurityConfig(RestAccessDeniedHandler restAccessDeniedHandler, CustomLoggingFilter customLoggingFilter) {
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.customLoggingFilter = customLoggingFilter;
    }

    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLoggingFilter customLoggingFilter) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .exceptionHandling(ex -> ex.accessDeniedHandler(restAccessDeniedHandler))
//                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless APIs
                .csrf(csrf -> csrf.disable())
                // All requests require authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Stateless session management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Exception handling
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                // Configure resource server (JWT validation)
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults())
                );

        // Add your custom logging filter after JWT validation
        http.addFilterAfter(customLoggingFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    *//**
 * Define JwtDecoder to validate JWT tokens from Keycloak
 * Replace <KEYCLOAK-REALM> and <KEYCLOAK-URL> with your settings
 *//*
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri("https://localhost:8080/realms/school-realm/protocol/openid-connect/certs")
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }
}*/

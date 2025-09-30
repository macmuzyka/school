package com.school.configuration;

import com.school.service.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(RestAccessDeniedHandler restAccessDeniedHandler) {
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLoggingFilter customLoggingFilter) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedHandler(restAccessDeniedHandler))
                .oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/authorization/keycloak"))
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
//        http.addFilterAfter(customLoggingFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }
}

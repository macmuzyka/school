package com.school.configuration;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts Keycloak realm roles from the JWT into GrantedAuthorities.
 * Works with token structure:
 * {
 * "realm_access": {
 * "roles": ["user", "admin"]
 * },
 * ...
 * }
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Logger log = LoggerFactory.getLogger(KeycloakRealmRoleConverter.class);
    @NotNull
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaims();
        log.info("REALM ACCESS: ");
        log.info(realmAccess.toString());
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof Collection<?> rolesCollection)) {
            return Collections.emptyList();
        }

        return rolesCollection.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}

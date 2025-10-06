package com.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeycloakOidcUserService extends OidcUserService {
    public static final Logger log = LoggerFactory.getLogger(KeycloakOidcUserService.class);

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        Map<String, Object> claims = oidcUser.getClaims();
        Optional<Map<String, Object>> realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
        var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));

        List<GrantedAuthority> authorities = roles.stream()
                .flatMap(Collection::stream)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .map(GrantedAuthority.class::cast)
                .toList();
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}

package com.tgfcodes.upfile.infrastructure.security;

import com.tgfcodes.upfile.infrastructure.persistence.UserEntity;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@NullMarked
public class UserAuth implements UserDetails {

    private final transient UserEntity userEntity;

    @Getter
    private final UUID userId;

    public UserAuth(final UserEntity userEntity) {
        this.userEntity = userEntity;
        this.userId = userEntity.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getScopes()
                .stream()
                .filter(scope -> !scope.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
    }

    @Override
    public @Nullable String getPassword() {
        return userEntity.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isActive();
    }
}
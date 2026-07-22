package com.gabriel.cashcard_api.infra.security;

import com.gabriel.cashcard_api.entities.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public record CustomUserDetails(
        UUID id,
        String name,
        String email,
        String password,
        Collection<? extends GrantedAuthority> roles
) implements UserDetails {

    public static CustomUserDetails fromEntity(User user) {
      var roles = user.getRoles()
              .stream()
              .map(role -> new SimpleGrantedAuthority(role.getName()))
              .collect(Collectors.toSet());

      return new CustomUserDetails(
              user.getId(),
              user.getName(),
              user.getEmail(),
              user.getPassword(),
              roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}

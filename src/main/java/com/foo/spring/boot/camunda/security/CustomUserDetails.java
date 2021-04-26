package com.foo.spring.boot.camunda.security;

import com.foo.spring.boot.camunda.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;
    private List<String> groups;
    private Collection<? extends GrantedAuthority> roles;

    public CustomUserDetails(String username, List<String> groups) {
        this.username = username;
        this.groups = groups;
    }

    public CustomUserDetails(String username, List<String> groups, Collection<? extends GrantedAuthority> roles) {
        this.username = username;
        this.groups = groups;
        this.roles = roles;
    }

    public static CustomUserDetails build(User user) {
        return new CustomUserDetails(
                user.getUsername(),
                user.getGroups()
        );
    }

    public List<String> getGroups() {
        return groups;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails user = (CustomUserDetails) o;
        return Objects.equals(username, user.username);
    }
}

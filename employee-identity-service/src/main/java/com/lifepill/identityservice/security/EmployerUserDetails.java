package com.lifepill.identityservice.security;

import com.lifepill.identityservice.entity.Employer;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Custom UserDetails implementation wrapping the Employer entity.
 */
@AllArgsConstructor
public class EmployerUserDetails implements UserDetails {

    private final Employer employer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return role-based authorities with granular permissions
        return employer.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return employer.getEmployerPassword();
    }

    @Override
    public String getUsername() {
        return employer.getEmployerEmail();
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

    public Employer getEmployer() {
        return employer;
    }
}

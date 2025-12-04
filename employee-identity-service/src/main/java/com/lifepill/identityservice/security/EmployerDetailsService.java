package com.lifepill.identityservice.security;

import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for loading employer details.
 */
@Service
@RequiredArgsConstructor
public class EmployerDetailsService implements UserDetailsService {

    private final EmployerRepository employerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employer employer = employerRepository.findByEmployerEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new EmployerUserDetails(employer);
    }
}

package com.example.NewAuthenticationApplication.security;

import com.example.NewAuthenticationApplication.entity.Role;
import com.example.NewAuthenticationApplication.entity.User;
import com.example.NewAuthenticationApplication.repository.UserRepository;
import com.example.NewAuthenticationApplication.service.impl.LoginAttemptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    public CustomUserDetailsService(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        try {
            if (loginAttemptService.isBlocked()) {
                throw new RuntimeException("blocked");
            }
        } catch (ExecutionException e) {
            System.err.println("Error checking IP Status: " + e.getMessage());
        }

        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                return new org.springframework.security.core.userdetails.User(user.getEmail(),
                        user.getPassword(),
                        mapRolesToAuthorities(user.getRoles()));
            }else{
                throw new UsernameNotFoundException("Invalid username or password.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}


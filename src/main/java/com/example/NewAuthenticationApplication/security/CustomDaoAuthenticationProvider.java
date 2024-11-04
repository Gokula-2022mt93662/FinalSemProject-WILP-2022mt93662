package com.example.NewAuthenticationApplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.NewAuthenticationApplication.service.RSAService;

@Component
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    RSAService service;
    @Autowired
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Autowired
    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }
    
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String presentedPassword = authentication.getCredentials().toString();
        String modifiedPassword = modifyPassword(presentedPassword, userDetails);
        int length = userDetails.getPassword().length();

        if (!getPasswordEncoder().matches(modifiedPassword, userDetails.getPassword().substring(0,(length-28)))) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    private String modifyPassword(String rawPassword, UserDetails userDetails) {

        int length = userDetails.getPassword().length();
        String Password =rawPassword + userDetails.getPassword().substring(length-28);
        return Password;
            
        
    }
}

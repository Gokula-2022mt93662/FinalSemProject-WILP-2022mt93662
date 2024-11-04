package com.example.NewAuthenticationApplication.authentication;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.example.NewAuthenticationApplication.service.impl.LoginAttemptService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener implements 
  ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            try {
                loginAttemptService.loginFailed(request.getRemoteAddr());
            } catch (ExecutionException e1) {
                System.err.println("Error getting address" + e1.getMessage());
            }
        } else {
            try {
                loginAttemptService.loginFailed(xfHeader.split(",")[0]);
            } catch (ExecutionException e1) {
                System.err.println("Error getting address" + e1.getMessage());
            }
        }
    }
}
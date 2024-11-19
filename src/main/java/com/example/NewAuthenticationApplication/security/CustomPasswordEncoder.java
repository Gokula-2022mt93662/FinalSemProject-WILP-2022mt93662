package com.example.NewAuthenticationApplication.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import com.example.NewAuthenticationApplication.service.RSAService;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private final RSAService Service;

    public CustomPasswordEncoder(RSAService Service) {
        this.Service = Service;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            String CheckedPassword = hashPassword(rawPassword.toString());
            String encryptedPassword = Service.encrypt(CheckedPassword);
            String hashedPassword = hashPassword(encryptedPassword);
            return new BCryptPasswordEncoder(12).encode(hashedPassword);
            
        } catch (Exception e) {
            return "Encryption failure " + e.getMessage();
        }

    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String CheckedPassword = hashPassword(rawPassword.toString());
            String encryptedPassword = Service.encrypt(CheckedPassword);
            String hashedPassword = hashPassword(encryptedPassword);
            return new BCryptPasswordEncoder(12).matches(hashedPassword, encodedPassword);
        }
        catch (Exception e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}

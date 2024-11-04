package com.example.NewAuthenticationApplication.service;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class Salt {

    public String newSalt(){
        SecureRandom Random = new SecureRandom(); 
        byte[] salt = new byte[20]; 
        Random.nextBytes(salt);
        System.out.println("The salt is " + Base64.getEncoder().encodeToString(salt));
        return Base64.getEncoder().encodeToString(salt);
    }

}

package com.example.NewAuthenticationApplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class RSAKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] publicKey;

       // Getter for publicKey
       public byte[] getPublicKey() {
        return publicKey;
    }

    // Setter for publicKey
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }
}

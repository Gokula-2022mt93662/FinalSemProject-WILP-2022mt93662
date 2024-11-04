package com.example.NewAuthenticationApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.NewAuthenticationApplication.entity.RSAKey;


public interface RSAKeyRepository extends JpaRepository<RSAKey, Long> {
}
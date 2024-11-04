package com.example.NewAuthenticationApplication.repository;

import com.example.NewAuthenticationApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

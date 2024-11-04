package com.example.NewAuthenticationApplication.repository;

import com.example.NewAuthenticationApplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

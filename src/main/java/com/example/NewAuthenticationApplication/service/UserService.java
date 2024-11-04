package com.example.NewAuthenticationApplication.service;

import com.example.NewAuthenticationApplication.dto.UserDto;
import com.example.NewAuthenticationApplication.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto) throws Exception;

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    String encrypt(CharSequence rawPassword);
}

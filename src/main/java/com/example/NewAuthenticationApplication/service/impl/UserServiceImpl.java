package com.example.NewAuthenticationApplication.service.impl;

import com.example.NewAuthenticationApplication.dto.UserDto;
import com.example.NewAuthenticationApplication.entity.Role;
import com.example.NewAuthenticationApplication.entity.User;
import com.example.NewAuthenticationApplication.repository.RoleRepository;
import com.example.NewAuthenticationApplication.repository.UserRepository;
import com.example.NewAuthenticationApplication.security.CustomPasswordEncoder;
import com.example.NewAuthenticationApplication.service.Salt;
import com.example.NewAuthenticationApplication.service.UserService;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CustomPasswordEncoder passwordEncoder;
    private Salt salt;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CustomPasswordEncoder passwordEncoder,
                           Salt salt) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.salt = salt;
    }

    @Override
    public void saveUser(UserDto userDto) throws Exception{
        User user = new User();
        user.setName(userDto.getFirstName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encrypt(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public String encrypt(CharSequence rawPassword) {
        StringBuilder newPassword =  new StringBuilder(rawPassword);
        String Salt = salt.newSalt();
        newPassword.append(Salt);
        CharSequence password = newPassword;
        String encryptedPassword = passwordEncoder.encode(password)+Salt;
        return encryptedPassword;                
        }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

}

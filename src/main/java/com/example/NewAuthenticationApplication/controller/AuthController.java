package com.example.NewAuthenticationApplication.controller;

import com.example.NewAuthenticationApplication.dto.UserDto;
import com.example.NewAuthenticationApplication.entity.User;
import com.example.NewAuthenticationApplication.repository.UserRepository;
import com.example.NewAuthenticationApplication.service.UserService;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private UserService userService;
    private UserRepository UserRepo;

    public AuthController(UserService userService , UserRepository UserRepo) {
        this.userService = userService;
        this.UserRepo = UserRepo;
    }

    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) throws Exception{
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser.getName();
        User user = UserRepo.findByEmail(email);
        String Username = user.getName();
        model.addAttribute("Username", Username);
        model.addAttribute("emailAddress", email);
        return "users";
    }
}

package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.User;
import com.study.studyplatform.JavaFX.repository.UserRepository;
import com.study.studyplatform.JavaFX.services.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthController(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @PostMapping("/register")
    public boolean register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        if(userRepository.findByEmail(email) != null) {
            return false;
        }

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword_hash(passwordService.encodePassword(password));

        userRepository.save(u);

        return true;
    }


    @PostMapping("/login")
    public boolean login(@RequestParam String email,
                         @RequestParam String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) return false;
        return passwordService.checkPassword(password, user.getPassword_hash());
    }

}
package com.study.studyplatform.JavaFX.controllers;

import com.study.studyplatform.JavaFX.entity.User;
import com.study.studyplatform.JavaFX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.study.studyplatform.JavaFX.services.PasswordService;

@Controller
@RequestMapping(path="/api/users")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordService passwordService;


    @GetMapping(path="/register") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email, @RequestParam String password) {

        String hashedPassword = passwordService.encodePassword(password);

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        n.setPassword_hash(hashedPassword);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping(path="/remove/{id}")
    public @ResponseBody String removeUser(@PathVariable Integer id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        return "Removed";
    }
    @GetMapping(path="/update/{id}")
    public @ResponseBody String removeUser(@PathVariable Integer id, @RequestParam String name, @RequestParam String email, @RequestParam String passwordHash) {
        User user = userRepository.findById(id).get();
        user.setName(name);
        user.setEmail(email);
        user.setPassword_hash(passwordHash);
        userRepository.save(user);
        return "Updated";
    }
}

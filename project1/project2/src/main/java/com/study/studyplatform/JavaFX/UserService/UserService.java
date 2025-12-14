package com.study.studyplatform.JavaFX.UserService;

import com.study.studyplatform.JavaFX.entity.User;
import com.study.studyplatform.JavaFX.repository.UserRepository;
import com.study.studyplatform.JavaFX.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    public void registerUser(String name, String email, String password) {
        String hashed = passwordService.encodePassword(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword_hash(hashed);

        userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}


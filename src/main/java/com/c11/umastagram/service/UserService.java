package com.c11.umastagram.service;

import com.c11.umastagram.model.User;
import com.c11.umastagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Example methods - add your business logic here
    public User saveUser(User user) {
        // Add validation, e.g., check if email is unique
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Hash password if needed (use BCrypt)
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();  // Use built-in method
    }

    // Add more methods as needed, e.g., update, delete, custom queries
}
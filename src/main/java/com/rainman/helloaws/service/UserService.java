package com.rainman.helloaws.service;

import com.rainman.helloaws.entity.User;
import com.rainman.helloaws.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        log.debug("Creating user: {}", user);
        User savedUser = userRepository.save(user);
        log.info("User created with id: {}", savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User user) {
        return getUser(id).map(u -> {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            return userRepository.save(u);
        }).orElse(null);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUser(Long id) {
        log.debug("Searching user with id: {}", id);
        Optional<User> user = userRepository.findById(id);
        user.ifPresentOrElse(u -> log.info("User found: {}", u), () -> log.warn("User not found for id: {}", id));
        return user;
    }

    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }
}

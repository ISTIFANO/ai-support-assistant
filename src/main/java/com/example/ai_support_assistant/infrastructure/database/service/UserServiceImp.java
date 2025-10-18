package com.example.ai_support_assistant.infrastructure.database.service;

import com.example.ai_support_assistant.domain.service.IUserService;
import com.example.ai_support_assistant.infrastructure.database.entities.User;
import com.example.ai_support_assistant.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements IUserService {

        private final UserRepository userRepository;

        @Override
        public User createUser(User user) {
            log.info("Creating user: {}", user.getEmail());
            return userRepository.save(user);
        }

        @Override
        public User getUserById(Long id) {
            log.info("Fetching user by id: {}", id);
            return userRepository.findById(id).orElse(null);
        }

        @Override
        public User getUserByEmail(String email) {
            log.info("Fetching user by email: {}", email);
            return userRepository.findByEmail(email);
        }

        @Override
        public List<User> getAllUsers() {
            log.info("Fetching all users");
            return userRepository.findAll();
        }

        @Override
        public void deleteUser(Long id) {
            log.info("Deleting user with id: {}", id);
            userRepository.deleteById(id);
        }
    }


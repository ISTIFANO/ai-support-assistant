package com.example.ai_support_assistant.domain.service;

import com.example.ai_support_assistant.infrastructure.database.entities.User;

import java.util.List;

public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    void deleteUser(Long id);
}

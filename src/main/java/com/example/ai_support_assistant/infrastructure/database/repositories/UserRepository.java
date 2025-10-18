package com.example.ai_support_assistant.infrastructure.database.repositories;

import com.example.ai_support_assistant.infrastructure.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
User findById(long id);


}

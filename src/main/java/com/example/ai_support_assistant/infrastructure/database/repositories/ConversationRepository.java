package com.example.ai_support_assistant.infrastructure.database.repositories;

import com.example.ai_support_assistant.infrastructure.database.entities.Conversation;
import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long>{
    List<Conversation> findByUserId(Long userId);

}

package com.example.ai_support_assistant.infrastructure.database.repositories;

import com.example.ai_support_assistant.infrastructure.database.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByDateEnvoiAsc(Long conversationId);

    List<Message> findTop5ByConversationIdOrderByDateEnvoiDesc(Long conversationId);
}
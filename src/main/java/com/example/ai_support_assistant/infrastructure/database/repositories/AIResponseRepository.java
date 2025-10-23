package com.example.ai_support_assistant.infrastructure.database.repositories;

import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIResponseRepository extends JpaRepository<AIResponse, Long> {
    List<AIResponse> findByConversationId(Long conversationId);


    List<AIResponse> findByConversationIdOrderByDateGenerationDesc(Long conversationId);

    @Query("SELECT ar FROM AIResponse ar WHERE ar.conversation.id = :conversationId AND ar.scoreConfiance > :minScore ORDER BY ar.dateGeneration DESC")
    List<AIResponse> findHighConfidenceResponses(Long conversationId, double minScore);
}
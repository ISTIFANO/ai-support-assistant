package com.example.ai_support_assistant.domain.service;

import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;

import java.util.List;

public interface IAIResponseService {
    AIResponse saveAIResponse(AIResponse aiResponse);
    List<AIResponse> getResponsesByConversation(Long conversationId);
}

package com.example.ai_support_assistant.domain.service;

import com.example.ai_support_assistant.infrastructure.database.entities.Conversation;

import java.util.List;

public interface IConversationService {

    Conversation createConversation(Conversation conversation);
    Conversation getConversationById(Long id);
    List<Conversation> getConversationsByUser(Long userId);
    void deleteConversation(Long id);
}

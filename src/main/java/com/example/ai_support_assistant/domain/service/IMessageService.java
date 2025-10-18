package com.example.ai_support_assistant.domain.service;

import com.example.ai_support_assistant.infrastructure.database.entities.Message;

import java.util.List;

public interface IMessageService {
    Message createMessage(Message message);
    List<Message> getMessagesByConversation(Long conversationId);
    List<Message> getLastNMessages(Long conversationId, int n);
}

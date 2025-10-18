package com.example.ai_support_assistant.infrastructure.database.service;

import com.example.ai_support_assistant.domain.service.IMessageService;
import com.example.ai_support_assistant.infrastructure.database.entities.Message;
import com.example.ai_support_assistant.infrastructure.database.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(Message message) {
        log.info("Creating message for conversation id: {}", message.getConversation().getId());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByConversation(Long conversationId) {
        log.info("Fetching messages for conversation id: {}", conversationId);
        return messageRepository.findByConversationIdOrderByDateEnvoiAsc(conversationId);
    }

    @Override
    public List<Message> getLastNMessages(Long conversationId, int n) {
        log.info("Fetching last {} messages for conversation id: {}", n, conversationId);
        return messageRepository.findTop5ByConversationIdOrderByDateEnvoiDesc(conversationId);
    }
}


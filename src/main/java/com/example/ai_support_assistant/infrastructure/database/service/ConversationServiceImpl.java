package com.example.ai_support_assistant.infrastructure.database.service;

import com.example.ai_support_assistant.domain.service.IConversationService;

import com.example.ai_support_assistant.infrastructure.database.entities.Conversation;
import com.example.ai_support_assistant.infrastructure.database.repositories.ConversationRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements IConversationService {

    private final ConversationRepository conversationRepository;

    @Override
    public Conversation createConversation(Conversation conversation) {
        log.info("Creating conversation for user: {}", conversation.getUser().getEmail());
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation getConversationById(Long id) {
        log.info("Fetching conversation by id: {}", id);
        return conversationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Conversation> getConversationsByUser(Long userId) {
        log.info("Fetching conversations for user id: {}", userId);
        return conversationRepository.findByUserId(userId);
    }

    @Override
    public void deleteConversation(Long id) {
        log.info("Deleting conversation with id: {}", id);
        conversationRepository.deleteById(id);
    }
}

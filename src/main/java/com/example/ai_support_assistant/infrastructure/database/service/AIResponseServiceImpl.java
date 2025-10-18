package com.example.ai_support_assistant.infrastructure.database.service;

import com.example.ai_support_assistant.domain.service.IAIResponseService;
import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;
import com.example.ai_support_assistant.infrastructure.database.repositories.AIResponseRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIResponseServiceImpl implements IAIResponseService {

    private final AIResponseRepository aiResponseRepository;

    @Override
    public AIResponse saveAIResponse(AIResponse aiResponse) {
        log.info("Saving AI response for conversation id: {}", aiResponse.getConversation().getId());
        return aiResponseRepository.save(aiResponse);
    }

    @Override
    public List<AIResponse> getResponsesByConversation(Long conversationId) {
        log.info("Fetching AI responses for conversation id: {}", conversationId);
        return aiResponseRepository.findByConversationId(conversationId);
    }
}


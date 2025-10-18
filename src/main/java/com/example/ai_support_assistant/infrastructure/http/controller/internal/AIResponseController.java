package com.example.ai_support_assistant.infrastructure.http.controller.internal;

import com.example.ai_support_assistant.domain.service.IAIResponseService;
import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ai-responses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AIResponse", description = "Operations related to AI responses")
public class AIResponseController {

    private final IAIResponseService aiResponseService;

    @PostMapping
    @Operation(summary = "Save AI response")
    public AIResponse saveAIResponse(@RequestBody AIResponse aiResponse) {
        return aiResponseService.saveAIResponse(aiResponse);
    }

    @GetMapping("/conversation/{conversationId}")
    @Operation(summary = "Get all AI responses for a conversation")
    public List<AIResponse> getResponsesByConversation(@PathVariable Long conversationId) {
        return aiResponseService.getResponsesByConversation(conversationId);
    }
}

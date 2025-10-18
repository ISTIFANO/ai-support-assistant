package com.example.ai_support_assistant.infrastructure.http.controller.internal;

import com.example.ai_support_assistant.domain.service.IConversationService;
import com.example.ai_support_assistant.infrastructure.database.entities.Conversation;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conversation", description = "Operations related to conversations")
public class ConversationController {

    private final IConversationService conversationService;

    @PostMapping
    @Operation(summary = "Create a new conversation")
    public Conversation createConversation(@RequestBody Conversation conversation) {
        return conversationService.createConversation(conversation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get conversation by ID")
    public Conversation getConversationById(@PathVariable Long id) {
        return conversationService.getConversationById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all conversations of a user")
    public List<Conversation> getConversationsByUser(@PathVariable Long userId) {
        return conversationService.getConversationsByUser(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete conversation by ID")
    public void deleteConversation(@PathVariable Long id) {
        conversationService.deleteConversation(id);
    }
}

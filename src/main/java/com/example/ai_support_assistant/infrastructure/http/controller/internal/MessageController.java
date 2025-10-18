package com.example.ai_support_assistant.infrastructure.http.controller.internal;

import com.example.ai_support_assistant.domain.service.IMessageService;
import com.example.ai_support_assistant.infrastructure.database.entities.Message;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Message", description = "Operations related to messages")
public class MessageController {

    private final IMessageService messageService;

    @PostMapping
    @Operation(summary = "Create a new message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @GetMapping("/conversation/{conversationId}")
    @Operation(summary = "Get all messages of a conversation")
    public List<Message> getMessagesByConversation(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversation(conversationId);
    }
}


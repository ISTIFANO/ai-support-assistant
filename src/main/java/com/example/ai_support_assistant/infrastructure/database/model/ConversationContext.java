package com.example.ai_support_assistant.infrastructure.database.model;

import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationContext {
    private String conversationHistory;
    private String intent;
    private List<FAQ> relevantFAQs;
    private double confidenceScore;
    private int messageCount;
    private LocalDateTime lastMessageTime;
}
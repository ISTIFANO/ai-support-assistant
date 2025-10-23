package com.example.ai_support_assistant.infrastructure.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseRequest {
    private Long conversationId;
    private boolean autoSend=false;
}
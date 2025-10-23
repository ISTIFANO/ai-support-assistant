package com.example.ai_support_assistant.infrastructure.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AIResponseDTO {
    private Long id;
    private String response;
    private double confidenceScore;
    private boolean shouldSend;
    private String intent;
}
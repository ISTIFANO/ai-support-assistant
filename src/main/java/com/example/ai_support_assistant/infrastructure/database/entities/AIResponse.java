package com.example.ai_support_assistant.infrastructure.database.entities;

import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "airesponse")
public class AIResponse extends BaseEntity {
    @ManyToOne
    private Conversation conversation;

    @Column(columnDefinition = "TEXT")
    private String reponse; // réponse générée par AI

    private double scoreConfiance;
    private LocalDateTime dateGeneration = LocalDateTime.now();
}

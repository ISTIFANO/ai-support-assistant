package com.example.ai_support_assistant.infrastructure.database.entities;

import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseEntity {
    @ManyToOne
    private Conversation conversation;

    @ManyToOne
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private String senderType; // CLIENT, AGENT, AI

    private LocalDateTime dateEnvoi = LocalDateTime.now();
}
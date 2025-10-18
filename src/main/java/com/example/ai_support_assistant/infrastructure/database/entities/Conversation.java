package com.example.ai_support_assistant.infrastructure.database.entities;

import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversations")
public class Conversation extends BaseEntity {
    private String statut; // OUVERT, EN_COURS, FERME

    @ManyToOne
    private User user;

    private LocalDateTime dateDernierMessage;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<AIResponse> aiResponses = new ArrayList<>();
}

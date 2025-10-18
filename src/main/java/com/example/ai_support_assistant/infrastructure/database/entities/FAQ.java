package com.example.ai_support_assistant.infrastructure.database.entities;

import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faqs")
public class FAQ extends BaseEntity {
    private String question;
    @Column(columnDefinition = "TEXT")
    private String answer;
    private String userId;
}

package com.example.ai_support_assistant.infrastructure.database.entities;

import com.example.ai_support_assistant.infrastructure.database.entities.commun.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String name;
    private String email;
    private String phone;
private String address;

}

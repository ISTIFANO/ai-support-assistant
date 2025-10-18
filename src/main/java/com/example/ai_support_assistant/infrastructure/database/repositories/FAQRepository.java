package com.example.ai_support_assistant.infrastructure.database.repositories;


import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByCategorie(String categorie);
}
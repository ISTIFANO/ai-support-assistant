package com.example.ai_support_assistant.domain.service;

import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;

import java.util.List;

public interface IFAQService{
    FAQ createFAQ(FAQ faq);
    List<FAQ> getAllFAQs();
    List<FAQ> getFAQsByCategorie(String categorie);
    void deleteFAQ(Long id);
}

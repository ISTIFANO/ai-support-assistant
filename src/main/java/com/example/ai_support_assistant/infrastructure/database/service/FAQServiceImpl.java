package com.example.ai_support_assistant.infrastructure.database.service;

// FAQServiceImpl.java
import com.example.ai_support_assistant.domain.service.IFAQService;
import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import com.example.ai_support_assistant.infrastructure.database.repositories.FAQRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FAQServiceImpl implements IFAQService {

    private final FAQRepository faqRepository;

    @Override
    public FAQ createFAQ(FAQ faq) {
        log.info("Creating FAQ: {}", faq.getQuestion());
        return faqRepository.save(faq);
    }

    @Override
    public List<FAQ> getAllFAQs() {
        log.info("Fetching all FAQs");
        return faqRepository.findAll();
    }

    @Override
    public List<FAQ> getFAQsByCategorie(String categorie) {
        log.info("Fetching FAQs by categorie: {}", categorie);
        return faqRepository.findByCategorie(categorie);
    }

    @Override
    public void deleteFAQ(Long id) {
        log.info("Deleting FAQ with id: {}", id);
        faqRepository.deleteById(id);
    }
}

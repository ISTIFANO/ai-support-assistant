package com.example.ai_support_assistant.infrastructure.database.service;

import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;
import com.example.ai_support_assistant.infrastructure.database.entities.Conversation;
import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import com.example.ai_support_assistant.infrastructure.database.entities.Message;
import com.example.ai_support_assistant.infrastructure.database.model.ConversationContext;
import com.example.ai_support_assistant.infrastructure.database.repositories.AIResponseRepository;
import com.example.ai_support_assistant.infrastructure.database.repositories.ConversationRepository;
import com.example.ai_support_assistant.infrastructure.database.repositories.FAQRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIContextAnalysisService {

    private final ConversationRepository conversationRepository;
    private final FAQRepository faqRepository;
    private final AIResponseRepository aiResponseRepository;
    private final GeminiAIServiceImp geminiAIService; // ✅ Injection correcte du service Gemini

    @Value("${gemini.api.key}")
    private String apiKey;

    /**
     * Analyse la conversation et génère une réponse IA
     */
    @Transactional
    public AIResponse analyzeAndGenerateResponse(Long conversationId) {
        try {
            log.info("🔍 Début de l'analyse IA pour la conversation: {}", conversationId);

            Conversation conversation = conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new RuntimeException("Conversation non trouvée avec ID: " + conversationId));

            if (conversation.getMessages() == null || conversation.getMessages().isEmpty()) {
                throw new RuntimeException("Aucun message dans la conversation");
            }

            // Analyse du contexte
            ConversationContext context = analyzeConversationContext(conversation);
            log.info("✅ Contexte analysé - Intention: {}, Score: {}",
                    context.getIntent(), context.getConfidenceScore());

            // Génération de la réponse
            AIResponse generatedResponse = generateAIResponse(context, conversation);
            log.info("🤖 Réponse IA générée avec succès - ID: {}", generatedResponse.getId());

            return generatedResponse;

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'analyse de la conversation {}: {}", conversationId, e.getMessage(), e);
            throw new RuntimeException("Échec de la génération de réponse IA: " + e.getMessage());
        }
    }

    // 🔹 Analyse du contexte
    private ConversationContext analyzeConversationContext(Conversation conversation) {
        List<Message> messages = conversation.getMessages();
        String conversationHistory = buildConversationHistory(messages);

        String intent = analyzeIntent(conversationHistory);
        List<FAQ> relevantFAQs = findRelevantFAQs(intent, conversationHistory);

        double confidenceScore = calculateConfidenceScore(intent, relevantFAQs, conversationHistory);

        return ConversationContext.builder()
                .conversationHistory(conversationHistory)
                .intent(intent)
                .relevantFAQs(relevantFAQs)
                .confidenceScore(confidenceScore)
                .messageCount(messages.size())
                .lastMessageTime(getLastMessageTime(messages))
                .build();
    }

    private String buildConversationHistory(List<Message> messages) {
        return messages.stream()
                .sorted((m1, m2) -> m1.getDateEnvoi().compareTo(m2.getDateEnvoi()))
                .map(msg -> String.format("[%s] %s: %s",
                        msg.getDateEnvoi().toLocalTime(),
                        msg.getSenderType(),
                        msg.getContenu()))
                .collect(Collectors.joining("\n"));
    }

    // 🔹 Analyse de l’intention avec Gemini
    private String analyzeIntent(String conversationHistory) {
        try {
            String prompt = String.format("""
                Analyse cette conversation de support client et identifie l'intention principale.
                Réponds avec UN SEUL mot parmi: QUESTION, PROBLEME, RECLAMATION, INFORMATION, AUTRE.

                Conversation:
                %s

                Intention:""", conversationHistory);

            String intent = geminiAIService.generateContent(prompt).trim();
            return cleanAndValidateIntent(intent);

        } catch (Exception e) {
            log.warn("⚠️ Erreur lors de l'analyse d'intention: {}", e.getMessage());
            return "AUTRE";
        }
    }

    private String cleanAndValidateIntent(String intent) {
        String cleaned = intent.toUpperCase().replaceAll("[^A-Z]", "").trim();
        List<String> validIntents = List.of("QUESTION", "PROBLEME", "RECLAMATION", "INFORMATION", "AUTRE");

        if (validIntents.contains(cleaned)) return cleaned;
        if (cleaned.contains("QUESTION")) return "QUESTION";
        if (cleaned.contains("PROBLEM")) return "PROBLEME";
        if (cleaned.contains("RECLAM")) return "RECLAMATION";
        if (cleaned.contains("INFORM")) return "INFORMATION";

        return "AUTRE";
    }

    // 🔹 Recherche des FAQs pertinentes
    private List<FAQ> findRelevantFAQs(String intent, String conversationHistory) {
        try {
            List<FAQ> faqsByCategory = faqRepository.findByCategoryContainingIgnoreCase(intent);
            String searchKeywords = extractMainKeywords(conversationHistory);

            List<FAQ> faqsByKeywords = faqRepository
                    .findByQuestionContainingOrAnswerContaining(searchKeywords, searchKeywords);

            return faqsByCategory.stream()
                    .filter(faq -> !faqsByKeywords.contains(faq))
                    .limit(3)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("⚠️ Erreur lors de la recherche de FAQs: {}", e.getMessage());
            return List.of();
        }
    }

    private String extractMainKeywords(String text) {
        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\b(je|tu|il|nous|vous|ils|mon|ton|son|notre|votre|leur|est|sont|ai|as|a|avons|avez|ont)\\b", "")
                .trim();

        String[] words = cleaned.split("\\s+");
        return words.length > 0 ? words[0] : "";
    }

    // 🔹 Calcul du score
    private double calculateConfidenceScore(String intent, List<FAQ> relevantFAQs, String conversationHistory) {
        double baseScore = switch (intent) {
            case "QUESTION" -> 0.7;
            case "PROBLEME" -> 0.65;
            case "INFORMATION" -> 0.6;
            case "RECLAMATION" -> 0.55;
            default -> 0.5;
        };

        if (!relevantFAQs.isEmpty()) baseScore += Math.min(relevantFAQs.size() * 0.1, 0.3);
        int messageCount = conversationHistory.split("\n").length;
        if (messageCount > 1) baseScore += Math.min((messageCount - 1) * 0.05, 0.15);

        return Math.min(baseScore, 1.0);
    }

    // 🔹 Génération de la réponse finale
    private AIResponse generateAIResponse(ConversationContext context, Conversation conversation) {
        String prompt = buildResponsePrompt(context);
        String aiResponseText = geminiAIService.generateContent(prompt);

        AIResponse response = new AIResponse();
        response.setConversation(conversation);
        response.setReponse(aiResponseText);
        response.setScoreConfiance(context.getConfidenceScore());
        response.setDateGeneration(LocalDateTime.now());

        return aiResponseRepository.save(response);
    }

    private String buildResponsePrompt(ConversationContext context) {
        StringBuilder prompt = new StringBuilder("""
            Tu es un assistant de support client intelligent et empathique.
            Ton rôle est d'aider les clients de manière professionnelle et utile.

            Historique de la conversation:
            """).append(context.getConversationHistory()).append("\n\n");

        prompt.append("Intention détectée: ").append(context.getIntent()).append("\n\n");

        if (!context.getRelevantFAQs().isEmpty()) {
            prompt.append("Informations de référence utiles:\n");
            for (int i = 0; i < context.getRelevantFAQs().size(); i++) {
                FAQ faq = context.getRelevantFAQs().get(i);
                prompt.append(i + 1).append(". Q: ").append(faq.getQuestion())
                        .append("\n   R: ").append(faq.getAnswer()).append("\n\n");
            }
        }

        prompt.append("""
            En te basant sur la conversation ci-dessus, génère une réponse:
            - En français
            - Professionnelle et empathique
            - Pertinente et concise
            - Propose des solutions si possible
            - Termine par une question ouverte si approprié

            Réponse:""");

        return prompt.toString();
    }

    private LocalDateTime getLastMessageTime(List<Message> messages) {
        return messages.stream()
                .map(Message::getDateEnvoi)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
    }

    // 🔹 Analyse temps réel
    @Transactional
    public void triggerRealTimeAnalysis(Long conversationId) {
        new Thread(() -> {
            try {
                analyzeAndGenerateResponse(conversationId);
            } catch (Exception e) {
                log.error("Erreur en analyse temps réel: {}", e.getMessage());
            }
        }).start();
    }

    public List<AIResponse> getAIResponsesForConversation(Long conversationId) {
        return aiResponseRepository.findByConversationIdOrderByDateGenerationDesc(conversationId);
    }

    public void evaluateResponse(Long responseId, boolean wasHelpful, String feedback) {
        AIResponse response = aiResponseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Réponse IA non trouvée"));
        log.info("📝 Évaluation de la réponse {}: Utile={}, Feedback={}",
                responseId, wasHelpful, feedback);
    }
}

package com.example.ai_support_assistant.infrastructure.database.service;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiAIServiceImp {

    private final WebClient geminiWebClient;

    // Génère une réponse via Gemini 2.5 Pro
    public String generateContent(String prompt) {
        try {
            log.info(" Envoi du prompt à Gemini 2.5 Pro...");
            // Construire le body sous forme de Map (JSON)
            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            String endpoint = "/v1beta/models/gemini-2.5-pro:generateContent";
            Dotenv dotenv = Dotenv.load();

            Map<?, ?> response = geminiWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(endpoint)
                            .queryParam("key",  dotenv.get("API_KEY"))
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(s -> s.is4xxClientError(), clientResponse ->
                            clientResponse.bodyToMono(String.class).flatMap(err -> {
                                log.error("Erreur 4xx Gemini: {}", err);
                                return Mono.error(new RuntimeException("Erreur client Gemini: " + err));
                            }))
                    .onStatus(s -> s.is5xxServerError(), clientResponse ->
                            clientResponse.bodyToMono(String.class).flatMap(err -> {
                                log.error("Erreur 5xx Gemini: {}", err);
                                return Mono.error(new RuntimeException("Erreur serveur Gemini: " + err));
                            }))
                    .bodyToMono(Map.class)
                    .block();

            log.info("Réponse reçue de Gemini !");
            return extractText(response);

        } catch (WebClientResponseException e) {
            log.error("Erreur WebClient: statut={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Erreur Gemini: " + e.getMessage();
        } catch (Exception e) {
            log.error("Erreur inattendue: {}", e.getMessage(), e);
            return "Erreur lors de génération du contenu.";
        }
    }

    private String extractText(Map<?, ?> json) {
        try {
            var candidates = (java.util.List<?>) json.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "Aucune réponse.";
            Map<?, ?> first = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) first.get("content");
            var parts = (java.util.List<?>) content.get("parts");
            if (parts == null || parts.isEmpty()) return "Pas de texte.";
            Map<?, ?> part0 = (Map<?, ?>) parts.get(0);
            return part0.get("text").toString();
        } catch (Exception e) {
            log.error("Erreur extraction texte: {}", e.getMessage(), e);
            return "Erreur extraction réponse.";
        }
    }
}

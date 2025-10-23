package com.example.ai_support_assistant.infrastructure.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;
    private List<SafetySetting> safetySettings;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerationConfig {
        private double temperature;
        private int maxOutputTokens;
        private double topP;
        private int topK;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SafetySetting {
        private String category;
        private String threshold;
    }

    // Méthode utilitaire pour construire une requête Gemini propre
    public static GeminiRequest create(String prompt, double temperature, int maxTokens) {
        GeminiRequest request = new GeminiRequest();

        // Contenu
        Part part = new Part(prompt);
        Content content = new Content(List.of(part));
        request.setContents(List.of(content));

        // Paramètres de génération
        GenerationConfig config = new GenerationConfig(temperature, maxTokens, 0.8, 40);
        request.setGenerationConfig(config);

        // Paramètres de sécurité (optionnel)
        SafetySetting safety = new SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_MEDIUM_AND_ABOVE");
        request.setSafetySettings(List.of(safety));

        return request;
    }
}

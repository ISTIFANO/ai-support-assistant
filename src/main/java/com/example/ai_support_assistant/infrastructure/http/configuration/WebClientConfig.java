package com.example.ai_support_assistant.infrastructure.http.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class WebClientConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-pro}")
    private String model;

    @Value("${gemini.api-version:v1beta}")
    private String apiVersion;

    @Value("${gemini.base-url:https://generativelanguage.googleapis.com}")
    private String baseUrl;

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String buildGenerateContentUrl() {
        return String.format("/%s/models/%s:generateContent?key=%s",
                apiVersion, model, apiKey);
    }
}

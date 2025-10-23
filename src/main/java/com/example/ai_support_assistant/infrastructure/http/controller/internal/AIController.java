package com.example.ai_support_assistant.infrastructure.http.controller.internal;

import com.example.ai_support_assistant.infrastructure.database.entities.AIResponse;
import com.example.ai_support_assistant.infrastructure.database.model.AIResponseDTO;
import com.example.ai_support_assistant.infrastructure.database.model.AIResponseRequest;
import com.example.ai_support_assistant.infrastructure.database.service.AIContextAnalysisService;
import com.example.ai_support_assistant.infrastructure.database.service.GeminiAIServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIContextAnalysisService aiContextAnalysisService;
    private final GeminiAIServiceImp geminiAIService;

    @PostMapping("/generate-response")
    public ResponseEntity<AIResponseDTO> generateAIResponse(@RequestBody AIResponseRequest request) {
        try {
            AIResponse aiResponse = aiContextAnalysisService.analyzeAndGenerateResponse(
                    request.getConversationId());

            AIResponseDTO responseDTO = AIResponseDTO.builder()
                    .id(aiResponse.getId())
                    .response(aiResponse.getReponse())
                    .confidenceScore(aiResponse.getScoreConfiance())
                    .shouldSend(aiResponse.getScoreConfiance() > 0.7)
                    .intent("ANALYZED") // Vous pouvez extraire l'intention réelle
                    .build();

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Erreur dans le controller: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/test-gemini")
    public Map<String, String> testGemini(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        String response = geminiAIService.generateContent(prompt);
        return Map.of("prompt", prompt, "response", response);
    }


    @GetMapping("")
    public String getMssg()  {
        return "this is controller";
    }
}


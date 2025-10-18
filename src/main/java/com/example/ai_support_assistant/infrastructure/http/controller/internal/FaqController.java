package com.example.ai_support_assistant.infrastructure.http.controller.internal;

import com.example.ai_support_assistant.domain.service.IFAQService;
import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "FAQS", description = "Operations related to FAQs")
public class FaqController {

    private final IFAQService faqService;

    @Operation(summary = "Create a new FAQ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FAQ created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<FAQ> createFAQ(@RequestBody FAQ faq) {
        FAQ createdFAQ = faqService.createFAQ(faq);
        return new ResponseEntity<>(createdFAQ, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all FAQs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQs fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<FAQ>> getAllFAQs() {
        List<FAQ> faqs = faqService.getAllFAQs();
        return ResponseEntity.ok(faqs);
    }

    @Operation(summary = "Get FAQs by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQs fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No FAQs found for the category")
    })
    @GetMapping("/category/{categorie}")
    public ResponseEntity<List<FAQ>> getFAQsByCategorie(@PathVariable String categorie) {
        List<FAQ> faqs = faqService.getFAQsByCategorie(categorie);
        if (faqs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faqs);
    }

    @Operation(summary = "Delete a FAQ by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FAQ deleted successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }
}

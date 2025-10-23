package com.example.ai_support_assistant.infrastructure.database.repositories;



import com.example.ai_support_assistant.infrastructure.database.entities.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    // === Recherche par catégorie ===

    /**
     * Trouve toutes les FAQs par catégorie exacte
     */
    List<FAQ> findByCategory(String category);

    /**
     * Trouve les FAQs par catégorie (insensible à la casse)
     */
    List<FAQ> findByCategoryIgnoreCase(String category);

    /**
     * Trouve les FAQs dont la catégorie contient le texte donné
     */
    List<FAQ> findByCategoryContaining(String category);

    /**
     * Trouve les FAQs dont la catégorie contient le texte donné (insensible à la casse)
     */
    List<FAQ> findByCategoryContainingIgnoreCase(String category);

    // === Recherche par question ===

    /**
     * Trouve les FAQs par question exacte
     */
    List<FAQ> findByQuestion(String question);

    /**
     * Trouve les FAQs dont la question contient le texte donné
     */
    List<FAQ> findByQuestionContaining(String question);

    /**
     * Trouve les FAQs dont la question contient le texte donné (insensible à la casse)
     */
    List<FAQ> findByQuestionContainingIgnoreCase(String question);

    // === Recherche par réponse ===

    /**
     * Trouve les FAQs dont la réponse contient le texte donné
     */
    List<FAQ> findByAnswerContaining(String answer);

    /**
     * Trouve les FAQs dont la réponse contient le texte donné (insensible à la casse)
     */
    List<FAQ> findByAnswerContainingIgnoreCase(String answer);

    // === Recherches combinées ===

    /**
     * Trouve les FAQs par question OU réponse contenant le texte donné
     */
    List<FAQ> findByQuestionContainingOrAnswerContaining(String question, String answer);

    /**
     * Trouve les FAQs par question OU réponse contenant le texte donné (insensible à la casse)
     */
    List<FAQ> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(String question, String answer);

    /**
     * Trouve les FAQs par catégorie ET question contenant le texte donné
     */
    List<FAQ> findByCategoryAndQuestionContaining(String category, String question);

    // === Recherches avancées avec @Query ===

    /**
     * Recherche full-text dans les questions et réponses
     */
    @Query("SELECT f FROM FAQ f WHERE " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FAQ> findByKeyword(@Param("keyword") String keyword);

    /**
     * Recherche full-text avec priorité sur les questions
     */
    @Query("SELECT f FROM FAQ f WHERE " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY " +
            "CASE WHEN LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 1 ELSE 2 END")
    List<FAQ> findByKeywordWithPriority(@Param("keyword") String keyword);

    /**
     * Recherche par multiple keywords
     */
    @Query("SELECT f FROM FAQ f WHERE " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword1, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword1, '%')) OR " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword2, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword2, '%'))")
    List<FAQ> findByMultipleKeywords(@Param("keyword1") String keyword1,
                                     @Param("keyword2") String keyword2);

    /**
     * Recherche avec scoring de pertinence
     */

    /**
     * Recherche par catégorie avec pagination implicite
     */
    @Query("SELECT f FROM FAQ f WHERE f.category = :category ORDER BY f.question")
    List<FAQ> findByCategoryOrderByQuestion(@Param("category") String category);

    /**
     * Trouve les FAQs créées par un utilisateur spécifique
     */
    List<FAQ> findByUserId(String userId);

    /**
     * Trouve les FAQs par catégorie et utilisateur
     */
    List<FAQ> findByCategoryAndUserId(String category, String userId);

    /**
     * Compte le nombre de FAQs par catégorie
     */
    @Query("SELECT f.category, COUNT(f) FROM FAQ f GROUP BY f.category")
    List<Object[]> countFAQsByCategory();

    /**
     * Trouve les catégories distinctes
     */
    @Query("SELECT DISTINCT f.category FROM FAQ f ORDER BY f.category")
    List<String> findDistinctCategories();

    /**
     * Recherche avancée avec multiple critères
     */
    @Query("SELECT f FROM FAQ f WHERE " +
            "(:category IS NULL OR f.category = :category) AND " +
            "(:keyword IS NULL OR LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<FAQ> advancedSearch(@Param("category") String category,
                             @Param("keyword") String keyword);

    /**
     * Trouve les FAQs similaires (même catégorie mais ID différent)
     */
    @Query("SELECT f FROM FAQ f WHERE f.category = :category AND f.id != :excludeId")
    List<FAQ> findSimilarFAQs(@Param("category") String category,
                              @Param("excludeId") Long excludeId);

    // === Méthodes de recherche pour l'IA ===

    /**
     * Recherche pour le système IA - priorise les FAQs avec un score de pertinence
     */
    @Query("SELECT f FROM FAQ f WHERE " +
            "LOWER(f.question) LIKE LOWER(CONCAT('%', :intent, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :intent, '%')) OR " +
            "LOWER(f.category) LIKE LOWER(CONCAT('%', :intent, '%')) " +
            "ORDER BY " +
            "CASE WHEN LOWER(f.category) = LOWER(:intent) THEN 1 " +
            "     WHEN LOWER(f.question) LIKE LOWER(CONCAT('%', :intent, '%')) THEN 2 " +
            "     ELSE 3 END")
    List<FAQ> findForAIAnalysis(@Param("intent") String intent);

    /**
     * Recherche les FAQs les plus pertinentes pour un contexte donné
     */
    @Query(value =
            "SELECT *, " +
                    "(CASE WHEN LOWER(question) LIKE LOWER(CONCAT('%', :keyword1, '%')) THEN 3 ELSE 0 END + " +
                    "CASE WHEN LOWER(answer) LIKE LOWER(CONCAT('%', :keyword1, '%')) THEN 2 ELSE 0 END + " +
                    "CASE WHEN LOWER(question) LIKE LOWER(CONCAT('%', :keyword2, '%')) THEN 2 ELSE 0 END + " +
                    "CASE WHEN LOWER(answer) LIKE LOWER(CONCAT('%', :keyword2, '%')) THEN 1 ELSE 0 END) as relevance " +
                    "FROM faqs " +
                    "WHERE LOWER(question) LIKE LOWER(CONCAT('%', :keyword1, '%')) OR " +
                    "LOWER(answer) LIKE LOWER(CONCAT('%', :keyword1, '%')) OR " +
                    "LOWER(question) LIKE LOWER(CONCAT('%', :keyword2, '%')) OR " +
                    "LOWER(answer) LIKE LOWER(CONCAT('%', :keyword2, '%')) " +
                    "ORDER BY relevance DESC " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<FAQ> findMostRelevantFAQs(@Param("keyword1") String keyword1,
                                   @Param("keyword2") String keyword2,
                                   @Param("limit") int limit);
}
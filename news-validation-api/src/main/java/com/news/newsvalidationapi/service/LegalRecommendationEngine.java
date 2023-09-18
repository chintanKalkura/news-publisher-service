package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ValidationReport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

@Validated
@Service
public class LegalRecommendationEngine {
    final Map<String,String> authorAndReviewMappings = new HashMap<>();
    final String DEFAULT_MAPPING_KEY = "by rookie author";
    final int MINIMUM_DELAY_IN_SECONDS = 5;
    final int MAXIMUM_DELAY_IN_SECONDS = 10;
    @Autowired
    private ArticleValidationStatusRepository articleValidationStatusRepository;

    public LegalRecommendationEngine() {
        authorAndReviewMappings.put("by senior author","review_needed_once");
        authorAndReviewMappings.put("by rookie author","review_needed_multiple");
        authorAndReviewMappings.put("by expert author","review_not_needed");
    }

    public Boolean recommend(@Valid Article article,
                             @NotNull Consumer<ValidationReport> callbackForValidationReport) {
        processArticleAsynchronously(article, callbackForValidationReport);
        return true;
    }

    private void processArticleAsynchronously(Article article, Consumer<ValidationReport> callbackForValidationReport) {
        CompletableFuture<Void> asyncProcessor = CompletableFuture.runAsync(()->{
            try {
                int delayInSeconds = RandomGenerator.getDefault()
                        .nextInt(MINIMUM_DELAY_IN_SECONDS, MAXIMUM_DELAY_IN_SECONDS);
                Thread.sleep(delayInSeconds* 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            article.getId();
            callbackForValidationReport.accept(getValidationReport(article));
        });
    }

    private ValidationReport getValidationReport(Article article) {
        return authorAndReviewMappings.entrySet().stream()
                .filter((entry)->article.getText().contains(entry.getKey()))
                .findFirst()
                .map(opEntry->buildValidationReport(article.getId(), opEntry.getValue()))
                .orElse(buildValidationReport(article.getId(), authorAndReviewMappings.get(DEFAULT_MAPPING_KEY)));
    }

    private ValidationReport buildValidationReport(String articleId, String recommendationValue) {
        return new ValidationReport(getUUID(),
                articleValidationStatusRepository.findById(articleId).orElse(null),
                "recommendation:"+ recommendationValue);
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

}

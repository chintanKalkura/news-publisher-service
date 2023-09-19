package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationReport;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class NewsValidationService {
    private static final Logger LOGGER = LogManager.getLogger(NewsValidationService.class);
    @Autowired
    private ArticleValidationStatusRepository articleValidationStatusRepository;
    @Autowired
    private LegalRecommendationEngine legalRecommendationEngine;
    @Autowired
    private ValidationReportRepository validationReportRepository;
    @Autowired
    private PublisherApiClient publisherApiClient;

    public void validate(Article article) {
        LOGGER.info("Starting validation for Article ID: {}", article.getId());
        ArticleValidationStatus articleValidationStatus = ArticleValidationStatus
                                                            .builder()
                                                            .articleId(article.getId())
                                                            .validationStatus(ValidationStatus.RECEIVED)
                                                            .build();
        articleValidationStatusRepository.save(articleValidationStatus);

        LOGGER.info("Saved ArticleValidationStatus: {} in repository for articleId: {}",articleValidationStatus, article.getId());

        Boolean recommendationResult = legalRecommendationEngine.recommend(article, recommendationEngineCallback);

        if(recommendationResult) {
            var newStatus = articleValidationStatusRepository
                    .updateArticleValidationStatus(article.getId(), ValidationStatus.IN_REVIEW_LEGAL);

            LOGGER.info("Updated ArticleValidationStatus: {} in repository for articleId: {}", newStatus, article.getId());
        }
    }

    public ArticleValidationStatus getArticleValidationStatus(String articleId) {
        Optional<ArticleValidationStatus> articleValidationStatus = articleValidationStatusRepository.findById(articleId);

        if(articleValidationStatus.isPresent()) {
            LOGGER.info("Found ArticleValidationStatus in repository for articleId: {}",articleId);
            return articleValidationStatus.get();
        }
        else {
            LOGGER.info("Did not find ArticleValidationStatus in repository for articleId: {}",articleId);
            return null;//handle scenario.
        }
    }

    private final Consumer<ValidationReport> recommendationEngineCallback = (validationReport) -> {
        var vr = validationReportRepository.save(validationReport);
        LOGGER.info("Saved ValidationReport: {} in repository for articleId: {}", vr, vr.getValidationStatus().getArticleId());

        var newStatus = articleValidationStatusRepository
                .updateArticleValidationStatus(validationReport.getValidationStatus().getArticleId(),
                        ValidationStatus.FINISHED);
        LOGGER.info("Updated ArticleValidationStatus: {} in repository for articleId: {}", newStatus, newStatus.getArticleId());

        publisherApiClient.postValidationReport(validationReportRepository
                .findById(validationReport.getReportId()).orElse(null));
    };

    public Consumer<ValidationReport> getRecommendationEngineCallback() {
        return recommendationEngineCallback;
    }
}

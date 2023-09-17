package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationReport;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class NewsValidationService {
    @Autowired
    private ArticleValidationStatusRepository articleValidationStatusRepository;
    @Autowired
    private LegalRecommendationEngine legalRecommendationEngine;
    @Autowired
    private ValidationReportRepository validationReportRepository;
    @Autowired
    private PublisherApiClient publisherApiClient;

    public void validate(Article article) {
        ArticleValidationStatus articleValidationStatus = ArticleValidationStatus
                                                            .builder()
                                                            .articleId(article.getId())
                                                            .validationStatus(ValidationStatus.RECEIVED)
                                                            .build();
        articleValidationStatusRepository.save(articleValidationStatus);

        Boolean recommendationResult = legalRecommendationEngine.recommend(article, recommendationEngineCallback);

        if(recommendationResult)
            articleValidationStatusRepository
                    .updateArticleValidationStatus(article.getId(), ValidationStatus.IN_REVIEW_LEGAL);

    }

    public ArticleValidationStatus getArticleValidationStatus(String articleId) {
        Optional<ArticleValidationStatus> articleValidationStatus = articleValidationStatusRepository.findById(articleId);

        if(articleValidationStatus.isPresent())
            return articleValidationStatus.get();
        else
            return null;//handle scenario.
    }

    private final Consumer<ValidationReport> recommendationEngineCallback = (validationReport) -> {
        validationReportRepository.save(validationReport);
        articleValidationStatusRepository
                .updateArticleValidationStatus(validationReport.getArticleId(), ValidationStatus.FINISHED);
        publisherApiClient.postValidationReport(validationReport);
    };

    public Consumer<ValidationReport> getRecommendationEngineCallback() {
        return recommendationEngineCallback;
    }
}

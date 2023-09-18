package com.news.newsvalidationapi;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationReport;
import com.news.newsvalidationapi.dto.ValidationStatus;
import com.news.newsvalidationapi.service.ArticleValidationStatusRepository;
import org.mockito.Mock;

public class NewsValidationTestBase {
    @Mock
    protected ArticleValidationStatusRepository articleValidationStatusRepository;
    protected String articleId = "74b6a64c-5461-11ee-8c99-0242ac120002";
    protected String reportId = "76bb18c0-86c6-446e-884d-37550247d49d";
    protected Article getArticle(String articleId) {
        return new Article(articleId,"test-article","test-article-text");
    }

    protected Article getArticle() {
        return getArticle(this.articleId);
    }

    protected Article getArticleByAuthorType(String author) {
        return new Article(articleId,"test-article","test-article-text"+author);
    }

    protected ArticleValidationStatus getArticleValidationStatus(ValidationStatus validationStatus) {
        return ArticleValidationStatus
                .builder()
                .articleId(articleId)
                .validationStatus(validationStatus)
                .build();
    }

    protected ArticleValidationStatus getArticleValidationStatus() {
        return getArticleValidationStatus(ValidationStatus.IN_REVIEW_LEGAL);
    }

    protected ValidationReport getValidationReport(ValidationStatus validationStatus) {
        return new ValidationReport(reportId, getArticleValidationStatus(validationStatus), "recommendation");
    }

    protected ValidationReport getValidationReport() {
        return getValidationReport(ValidationStatus.IN_REVIEW_LEGAL);
    }

    protected ValidationReport getValidationReport(String recommendationValue) {
        return new ValidationReport(reportId,
                getArticleValidationStatus(),
                "recommendation"+":"+recommendationValue);
    }
}

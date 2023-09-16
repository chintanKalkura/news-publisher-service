package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.springframework.stereotype.Service;

@Service
public class NewsValidationService {

    public void validate(Article article) {
    }

    public ArticleValidationStatus getArticleValidationDetails(String articleId) {
        return new ArticleValidationStatus(articleId, ValidationStatus.IN_REVIEW_LEGAL_1);
    }

}

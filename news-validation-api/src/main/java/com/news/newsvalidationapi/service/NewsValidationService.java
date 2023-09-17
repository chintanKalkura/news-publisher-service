package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsValidationService {
    @Autowired
    private NewsValidationRepository newsValidationRepository;

    public void validate(Article article) {
    }

    public ArticleValidationStatus getArticleValidationDetails(String articleId) {
        return null;
    }

}

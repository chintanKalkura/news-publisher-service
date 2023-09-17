package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ValidationReport;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class LegalRecommendationEngine {
    public Boolean recommend(Article article,
                             Consumer<ValidationReport> callbackForValidationReport) {
        return null;
    }
}

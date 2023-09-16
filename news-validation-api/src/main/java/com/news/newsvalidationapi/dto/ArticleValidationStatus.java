package com.news.newsvalidationapi.dto;

import lombok.Value;

@Value
public class ArticleValidationStatus {
    String articleId;
    ValidationStatus validationStatus;
}

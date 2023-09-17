package com.news.newsvalidationapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Entity
public class ArticleValidationStatus {
    @Id
    String articleId;
    @Enumerated
    ValidationStatus validationStatus;
}

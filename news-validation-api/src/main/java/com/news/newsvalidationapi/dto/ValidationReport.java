package com.news.newsvalidationapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Value;

/*
* TODO: Create a relationship between reportId and ArticleId in status entity*/

@Value
@Entity
public class ValidationReport {
    @Id
    String reportId;
    String articleId;
    String recommendation;
}

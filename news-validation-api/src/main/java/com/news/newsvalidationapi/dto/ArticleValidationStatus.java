package com.news.newsvalidationapi.dto;

import jakarta.persistence.*;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reportId")
    ValidationReport validationReport;
}

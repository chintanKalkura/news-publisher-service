package com.news.newsvalidationapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Value;

@Value
@Entity
public class ValidationReport {
    @Id
    String reportId;
    @OneToOne(mappedBy = "validationReport")
    ArticleValidationStatus validationStatus;
    String recommendation;
}

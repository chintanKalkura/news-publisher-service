package com.news.newsvalidationapi;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.mockito.ArgumentMatcher;

public class ValidationReportMatcher implements ArgumentMatcher<ValidationReport> {
    private final ValidationReport left;

    public ValidationReportMatcher(ValidationReport left) {
        this.left = left;
    }

    @Override
    public boolean matches(ValidationReport right) {
        return left.getRecommendation().equals(right.getRecommendation());
    }
}

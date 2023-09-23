package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationReportRepository extends JpaRepository<ValidationReport, String> {
    Optional<ValidationReport> findByArticleId(String articleId);
}

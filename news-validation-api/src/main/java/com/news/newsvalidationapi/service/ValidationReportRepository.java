package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationReportRepository extends JpaRepository<ValidationReport, String> {
}

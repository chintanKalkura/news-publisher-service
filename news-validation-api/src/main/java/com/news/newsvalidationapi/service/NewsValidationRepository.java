package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsValidationRepository extends JpaRepository<ArticleValidationStatus,String> {
}

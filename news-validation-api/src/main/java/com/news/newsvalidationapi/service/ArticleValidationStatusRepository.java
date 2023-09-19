package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleValidationStatusRepository extends JpaRepository<ArticleValidationStatus,String> {
    @Query("UPDATE ArticleValidationStatus " +
            "SET validationStatus=(:status) " +
            "WHERE articleId=(:articleId)")
    ArticleValidationStatus updateArticleValidationStatus(@Param("articleId") String articleId, @Param("status") ValidationStatus status);
}

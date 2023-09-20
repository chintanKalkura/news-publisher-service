package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleValidationStatusRepository extends JpaRepository<ArticleValidationStatus,String> {
    @Modifying
    @Transactional
    @Query("UPDATE ArticleValidationStatus " +
            "SET validationStatus=(:status) " +
            "WHERE articleId=(:articleId)")
    void updateArticleValidationStatus(@Param("articleId") String articleId, @Param("status") ValidationStatus status);
}

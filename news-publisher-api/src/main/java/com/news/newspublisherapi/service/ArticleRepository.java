package com.news.newspublisherapi.service;

import com.news.newspublisherapi.dto.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,String> {
}

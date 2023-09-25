package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,String> {
}

package com.news.newspublisherapi.service;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetResourceService {
    @Autowired
    private ArticleRepository articleRepository;

    @Cacheable(cacheNames = "publishedArticles", key="#articleStatus", condition = "#articleStatus.name() == 'PUBLISHED'")
    public List<Article> findAll(ArticleStatus articleStatus) {
        return articleRepository.findAll().stream()
                .filter(article -> article.getArticleStatus().equals(articleStatus))
                .collect(Collectors.toList());
    }

    @Cacheable("articlesById")
    public Optional<Article> findById(String articleId) {
        return articleRepository.findById(articleId);
    }
}

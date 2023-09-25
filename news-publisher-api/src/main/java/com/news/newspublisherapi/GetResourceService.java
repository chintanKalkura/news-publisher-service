package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetResourceService {
    @Autowired
    private ArticleRepository articleRepository;

    @Cacheable("findAll")
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Cacheable("findById")
    public Optional<Article> findById(String articleId) {
        return articleRepository.findById(articleId);
    }
}

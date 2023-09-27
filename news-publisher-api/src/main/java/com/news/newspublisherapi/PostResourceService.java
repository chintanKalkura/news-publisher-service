package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostResourceService {
    @Autowired
    private ArticleRepository articleRepository;
    public void updateNewsArticleStatus(String articleId, ArticleStatus articleStatus) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);

        articleOptional.ifPresent(article -> {  article.setArticleStatus(ArticleStatus.PUBLISHED);
                                                articleRepository.save(article);
        });
    }
}

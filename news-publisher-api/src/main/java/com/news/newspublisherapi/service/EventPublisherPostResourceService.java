package com.news.newspublisherapi.service;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventPublisherPostResourceService extends CacheablePostResourceService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void updateNewsArticleStatus(String articleId, ArticleStatus articleStatus) {
        super.updateNewsArticleStatus(articleId, articleStatus);
        if(articleStatus == ArticleStatus.PUBLISHED) {
            Optional<Article> article = articleRepository.findById(articleId);
            article.ifPresent(value -> kafkaTemplate.send("publishedArticles", value.toString()));
        }
    }
}

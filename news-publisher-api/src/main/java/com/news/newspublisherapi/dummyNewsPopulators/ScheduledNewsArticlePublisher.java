package com.news.newspublisherapi.dummyNewsPopulators;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import com.news.newspublisherapi.service.ArticleRepository;
import com.news.newspublisherapi.service.EventPublisherPostResourceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledNewsArticlePublisher {
    private final Logger LOGGER = LogManager.getLogger(ScheduledNewsArticlePublisher.class);
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EventPublisherPostResourceService kafkaPostResourceService;
    private static Integer count = 11;

    @Scheduled(timeUnit = TimeUnit.MINUTES, initialDelay = 1 ,fixedDelay = 2)
    public void publishArticlesOnSchedule() {
        String articleId = addNewArticleToDb();
        LOGGER.info("Added new article to DB. Article ID: {}", articleId);
        kafkaPostResourceService.updateNewsArticleStatus(articleId, ArticleStatus.PUBLISHED);
        LOGGER.info("Updated Article Status for article. Article ID: {}", articleId);
    }

    private String addNewArticleToDb() {
        String articleId = UUID.randomUUID().toString();
        articleRepository.save(new Article(articleId,
                "name - "+ ScheduledNewsArticlePublisher.count,
                "text - "+ ScheduledNewsArticlePublisher.count,
                ArticleStatus.REVIEWED_EDITOR,
                ScheduledNewsArticlePublisher.count %2 == 0 ?
                        "recommendation : some recommendation"+ ScheduledNewsArticlePublisher.count :
                        null));
        ScheduledNewsArticlePublisher.count++;
        return articleId;
    }
}

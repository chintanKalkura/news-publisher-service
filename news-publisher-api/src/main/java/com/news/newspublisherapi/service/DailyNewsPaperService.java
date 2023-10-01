package com.news.newspublisherapi.service;

import com.news.newspublisherapi.dto.ArticleStatus;
import com.news.newspublisherapi.dto.DailyNewsPaper;
import com.news.newspublisherapi.dto.DailyNewsPaperKey;
import com.news.newspublisherapi.dto.PublishedArticle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class DailyNewsPaperService {
    private final Logger LOGGER = LogManager.getLogger(DailyNewsPaperService.class);
    private final static List<String> REGION_LIST = List.of("mysore", "bangalore", "mangalore", "hubli");
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private DailyNewsPaperRepository dailyNewsPaperRepository;
    @Scheduled(timeUnit = TimeUnit.MINUTES, initialDelay = 10, fixedDelay = 24*60)
    public void makeDailyNewsPaper() {
        List<PublishedArticle> todaysPublishedArticles = articleRepository.findAll().stream()
                        .filter(article -> article.getArticleStatus().equals(ArticleStatus.PUBLISHED))
                        .map(article -> PublishedArticle.builder()
                                .id(article.getId())
                                .text(article.getText())
                                .name(article.getName())
                                .build())
                                .toList();

        REGION_LIST.forEach(region-> {
            LOGGER.info("Starting to make daily newspaper for date : {} and region : {}", LocalDate.now(),region);
            var dailyNewsPaper = buildDailyNewsPaperFor(region, todaysPublishedArticles);
            dailyNewsPaperRepository.save(dailyNewsPaper);
            LOGGER.info("Saved daily newspaper : {} ", dailyNewsPaper);
        });
    }

    private DailyNewsPaper buildDailyNewsPaperFor(String region, List<PublishedArticle> todaysPublishedArticles) {
        return new DailyNewsPaper(new DailyNewsPaperKey(Date.from(Instant.now()), region),
                Set.copyOf(todaysPublishedArticles),
                Set.copyOf(todaysPublishedArticles),
                Set.copyOf(todaysPublishedArticles),
                Set.copyOf(todaysPublishedArticles),
                Set.copyOf(todaysPublishedArticles));
    }
}

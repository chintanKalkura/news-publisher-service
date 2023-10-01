package com.news.newspublisherapi.dummyNewsPopulators;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import com.news.newspublisherapi.service.ArticleRepository;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ArticleInitialiser {
    private final Logger LOGGER = LogManager.getLogger(ArticleInitialiser.class);
    @Autowired
    private ArticleRepository articleRepository;

    @PostConstruct
    public void initalise() {
        LOGGER.info("Initialise called in class Article Initialiser. Autowired ArticleRepository : {}",articleRepository.getClass());
        for(int i=0; i<10; i++) {
            Article article = getArticle(i);
            articleRepository.save(article);
            LOGGER.info("Article : {} saved in repository", article);
        }
    }

    private Article getArticle(int iter) {
        Article article =  new Article(UUID.randomUUID().toString(),
                        "name "+iter,
                        "some text "+iter,
                        getArticleStatus(iter),
                        null);
        if(article.getArticleStatus() == ArticleStatus.REVIEWED_EDITOR) {
            article.setRecommendation("recommendation : some recommendation");
        }
        return article;
    }

    private ArticleStatus getArticleStatus(int iter) {
        //IN_REVIEW_EDITOR, REVIEWED_LEGAL, REVIEWED_EDITOR, IN_RE_REVIEW, PUBLISHED
        if(iter < 2)
            return ArticleStatus.IN_REVIEW_EDITOR;
        if(iter < 4)
            return ArticleStatus.REVIEWED_LEGAL;
        if(iter < 6)
            return ArticleStatus.REVIEWED_EDITOR;
        if(iter < 8)
            return ArticleStatus.IN_RE_REVIEW;
        return ArticleStatus.PUBLISHED;
    }
}

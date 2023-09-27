package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.ArticleStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheablePostResourceService extends PostResourceService{

    @Override
    @CacheEvict(cacheNames = "publishedArticles", condition = "#articleStatus.name() == 'PUBLISHED'", allEntries = true)
    //TODO : instead of cache evict append the new published article to the cache.
    public void updateNewsArticleStatus(String articleId, ArticleStatus articleStatus) {
        super.updateNewsArticleStatus(articleId, articleStatus);
    }
}

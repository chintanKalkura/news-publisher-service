package com.news.newsvalidationapi.controller;

import com.news.newsvalidationapi.CONSTANTS;
import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.service.NewsValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsValidationController {
    @Autowired
    private NewsValidationService newsValidationService;

    @PostMapping("/news/validation/{articleId}")
    public ResponseEntity<HttpStatus> validateNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {
        if(isValid(articleId))
            return ResponseEntity.badRequest().build();

        newsValidationService.validate(article);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/news/validation/{articleId}")
    public ResponseEntity<ArticleValidationStatus> getValidationStatus(@PathVariable String articleId) {
        if(isValid(articleId))
            return ResponseEntity.badRequest().build();

        ArticleValidationStatus validationStatus  = newsValidationService.getArticleValidationStatus(articleId);
        if(validationStatus == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(validationStatus);
    }

    private static boolean isValid(String articleId) {
        return !articleId.matches(CONSTANTS.UUID_REG_EXP);
    }
}

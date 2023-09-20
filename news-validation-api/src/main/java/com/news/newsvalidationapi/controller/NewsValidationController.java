package com.news.newsvalidationapi.controller;

import com.news.newsvalidationapi.CONSTANTS;
import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationReport;
import com.news.newsvalidationapi.service.NewsValidationService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsValidationController {
    private static final Logger LOGGER = LogManager.getLogger(NewsValidationController.class);
    @Autowired
    private NewsValidationService newsValidationService;

    @PostMapping("/news/validation/article/{articleId}")
    public ResponseEntity<HttpStatus> validateNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {
        LOGGER.info("Received validation request for Article ID: {} and request : {}",articleId, article);
        if(isValid(articleId))
            return ResponseEntity.badRequest().build();

        newsValidationService.validate(article);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/news/validation/article/{articleId}")
    @ResponseBody
    public ResponseEntity<ArticleValidationStatus> getValidationStatus(@PathVariable String articleId) {
        if(isValid(articleId))
            return ResponseEntity.badRequest().build();

        ArticleValidationStatus validationStatus  = newsValidationService.getArticleValidationStatus(articleId);
        if(validationStatus == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(validationStatus);
    }

    @GetMapping("/news/validation/article/{articleId}/report")
    @ResponseBody
    public ResponseEntity<ValidationReport> getValidationReport(@PathVariable String articleId) {
        if(isValid(articleId))
            return ResponseEntity.badRequest().build();

        ValidationReport validationReport  = newsValidationService.getArticleValidationReport(articleId);
        if(validationReport == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(validationReport);
    }

    private static boolean isValid(String articleId) {
        return !articleId.matches(CONSTANTS.UUID_REG_EXP);
    }
}

package com.news.newsvalidationapi.controller;

import com.news.newsvalidationapi.CONSTANTS;
import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.service.NewsValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsValidationController {
    @Autowired
    private NewsValidationService newsValidationService;

    @PostMapping("/news/validation/{articleId}")
    public ResponseEntity<HttpStatus> validateNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {
        if(!articleId.matches(CONSTANTS.UUID_REG_EXP))
            return ResponseEntity.badRequest().build();

        newsValidationService.validate(article);
        return ResponseEntity.accepted().build();
    }
}

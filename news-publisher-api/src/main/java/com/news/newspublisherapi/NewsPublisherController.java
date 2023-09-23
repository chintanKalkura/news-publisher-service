package com.news.newspublisherapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NewsPublisherController {

    @PostMapping("news/publish/article/{articleId}")
    public ResponseEntity<HttpStatus> submitNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {

        return ResponseEntity.accepted().build();
    }

    @PostMapping("news/publish/article/{articleId}/recommend")
    public ResponseEntity<HttpStatus> submitNewsArticle(@PathVariable String articleId,
                                                        @Valid @RequestBody String recommendation) {

        return ResponseEntity.accepted().build();
    }

    @PutMapping("news/publish/article/{articleId}")
    public ResponseEntity<HttpStatus> updateNewsArticle(@PathVariable String articleId,
                                                        @Valid @RequestBody Article article) {

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("news/publish/article/{articleId}/status/{articleStatus}")
    public ResponseEntity<HttpStatus> updateNewsArticleStatus(@PathVariable String articleId,
                                                        @PathVariable ArticleStatus articleStatus) {

        return ResponseEntity.accepted().build();
    }

    @GetMapping("news/publish/article/{articleStatus}")
    @ResponseBody
    public ResponseEntity<List<Article>> getArticlesByStatus(@PathVariable ArticleStatus articleStatus) {

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/news/publish/article/{articleId}")
    @ResponseBody
    public ResponseEntity<Article> getValidationStatus(@PathVariable String articleId) {

        return ResponseEntity.ok(null);
    }
}

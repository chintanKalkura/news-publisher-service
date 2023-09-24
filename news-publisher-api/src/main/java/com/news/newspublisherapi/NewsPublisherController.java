package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class NewsPublisherController {
    private final Logger LOGGER = LogManager.getLogger(NewsPublisherController.class);

    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("news/publish/article/{articleId}")
    @RolesAllowed("AUTHOR")
    public ResponseEntity<HttpStatus> submitNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {
        return ResponseEntity.accepted().build();
    }

    @PostMapping("news/publish/article/{articleId}/recommend")
    @RolesAllowed("EDITOR")
    public ResponseEntity<HttpStatus> submitRecommendationForNewsArticle(@PathVariable String articleId,
                                                        @NotEmpty @RequestBody String recommendation) {
        return ResponseEntity.accepted().build();
    }

    @PutMapping("news/publish/article/{articleId}")
    @RolesAllowed("AUTHOR")
    public ResponseEntity<HttpStatus> updateNewsArticle(@PathVariable String articleId,
                                                        @Valid @RequestBody Article article) {
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("news/publish/article/{articleId}/status/{articleStatus}")
    @RolesAllowed("EDITOR")
    public ResponseEntity<HttpStatus> updateNewsArticleStatus(@PathVariable String articleId,
                                                        @NotNull @PathVariable ArticleStatus articleStatus) {
        return ResponseEntity.accepted().build();
    }

    @GetMapping("news/publish/article/status/{articleStatus}")
    @ResponseBody
    @RolesAllowed({"EDITOR","AUTHOR","READER"})
    public ResponseEntity<List<Article>> getArticlesByStatus(@PathVariable ArticleStatus articleStatus, Authentication authentication) {
        if(!isUserAuthorisedForArticleStatus(articleStatus, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Article> articleList = articleRepository.findAll();
        return ResponseEntity.ok(
                articleList.stream()
                        .filter(article -> article.getArticleStatus().equals(articleStatus))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/news/publish/article/{articleId}")
    @ResponseBody
    @RolesAllowed({"EDITOR","AUTHOR"})
    public ResponseEntity<Article> getArticle(@PathVariable String articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/news/publish/article/{articleId}/status")
    @ResponseBody
    @RolesAllowed({"EDITOR","AUTHOR"})
    public ResponseEntity<ArticleStatus> getStatusOfArticle(@PathVariable String articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        return ResponseEntity.ok(article.map(Article::getArticleStatus).orElse(null));
    }

    private boolean isUserAuthorisedForArticleStatus(ArticleStatus articleStatus, Authentication authentication) {
        List<String> userRole = authentication.getAuthorities().stream()
                .toList().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        List<ArticleStatus> READER_ARTICLE_STATUS = List.of(ArticleStatus.PUBLISHED);
        List<ArticleStatus> AUTHOR_ARTICLE_STATUS = List.of(ArticleStatus.REVIEWED_EDITOR, ArticleStatus.PUBLISHED);
        List<ArticleStatus> EDITOR_ARTICLE_STATUS = List.of(ArticleStatus.IN_REVIEW_EDITOR,
                ArticleStatus.REVIEWED_LEGAL, ArticleStatus.IN_RE_REVIEW, ArticleStatus.REVIEWED_EDITOR, ArticleStatus.PUBLISHED);

        if(userRole.stream().anyMatch(s -> s.contains("READER")) && READER_ARTICLE_STATUS.contains(articleStatus))
            return true;
        if(userRole.stream().anyMatch(s -> s.contains("AUTHOR")) && AUTHOR_ARTICLE_STATUS.contains(articleStatus))
            return true;
        if(userRole.stream().anyMatch(s -> s.contains("EDITOR")) && EDITOR_ARTICLE_STATUS.contains(articleStatus))
            return true;
        return false;
    }
}

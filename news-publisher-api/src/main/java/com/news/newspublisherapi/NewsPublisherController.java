package com.news.newspublisherapi;

import com.news.newspublisherapi.dto.Article;
import com.news.newspublisherapi.dto.ArticleStatus;
import com.news.newspublisherapi.service.GetResourceService;
import com.news.newspublisherapi.service.PostResourceService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.news.newspublisherapi.dto.CONSTANTS.*;

@RestController
public class NewsPublisherController {
    private final Logger LOGGER = LogManager.getLogger(NewsPublisherController.class);
    @Autowired
    private GetResourceService getResourceService;
    @Autowired
    private PostResourceService postResourceService;

    @PostMapping("news/publish/article/{articleId}")
    public ResponseEntity<HttpStatus> submitNewsArticle(@PathVariable String articleId,
                                                          @Valid @RequestBody Article article) {
        return ResponseEntity.accepted().build();
    }

    @PostMapping("news/publish/article/{articleId}/recommend")
    public ResponseEntity<HttpStatus> submitRecommendationForNewsArticle(@PathVariable String articleId,
                                                        @NotEmpty @RequestBody String recommendation) {
        return ResponseEntity.accepted().build();
    }

    @PutMapping("news/publish/article/{articleId}")
    public ResponseEntity<HttpStatus> updateNewsArticle(@PathVariable String articleId,
                                                        @Valid @RequestBody Article article) {
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("news/publish/article/{articleId}/status/{articleStatus}")
    public ResponseEntity<HttpStatus> updateNewsArticleStatus(@PathVariable String articleId,
                                                        @NotNull @PathVariable ArticleStatus articleStatus) {
        postResourceService.updateNewsArticleStatus(articleId, articleStatus);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("news/publish/article/status/{articleStatus}")
    @ResponseBody
    public ResponseEntity<List<Article>> getArticlesByStatus(@PathVariable ArticleStatus articleStatus, Authentication authentication) {
        if(!isUserAuthorisedForArticleStatus(articleStatus, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Article> articleList = getResourceService.findAll(articleStatus);
        return ResponseEntity.ok(articleList);
    }

    @GetMapping("/news/publish/article/{articleId}")
    @ResponseBody
    public ResponseEntity<Article> getArticle(@PathVariable String articleId) {
        Article article = getResourceService.findById(articleId).orElse(null);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/news/publish/article/{articleId}/status")
    @ResponseBody
    public ResponseEntity<ArticleStatus> getStatusOfArticle(@PathVariable String articleId) {
        Optional<Article> article = getResourceService.findById(articleId);
        return ResponseEntity.ok(article.map(Article::getArticleStatus).orElse(null));
    }

    private boolean isUserAuthorisedForArticleStatus(ArticleStatus articleStatus, Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s ->
                        ((s.contains("READER") && READER_AUTHORISED_FOR_ARTICLE_STATUS.contains(articleStatus)) ||
                        (s.contains("AUTHOR") && AUTHOR_AUTHORISED_FOR_ARTICLE_STATUS.contains(articleStatus)) ||
                        (s.contains("EDITOR") && EDITOR_AUTHORISED_FOR_ARTICLE_STATUS.contains(articleStatus)))
                );
    }
}

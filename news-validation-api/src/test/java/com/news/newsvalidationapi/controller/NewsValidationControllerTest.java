package com.news.newsvalidationapi.controller;

import com.news.newsvalidationapi.NewsValidationTestBase;
import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.JwtResponse;
import com.news.newsvalidationapi.service.NewsValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class NewsValidationControllerTest extends NewsValidationTestBase {

    @MockBean
    private NewsValidationService mockNewsValidationService;

    @BeforeAll
    public void setUp() {
        JwtResponse jwtResponse = testRestTemplate.postForObject(
                URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);

        this.accessToken = jwtResponse.getAccessToken();
    }

    @Test
    public void shouldReceiveHttpStatusAccepted_forValidateRequest() {
        ResponseEntity<String> responseEntity  = postArticle(accessToken);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveHttpStatusUnauthorised_forValidateRequest_withInvalidAccessToken() {
        ResponseEntity<String> responseEntity = postArticle(invalidAccessToken);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveHttpStatusUnauthorised_forValidateRequest_withExpiredAccessToken() {
        ResponseEntity<String> responseEntity = postArticle(expiredAccessToken);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveHttpStatusBadRequest_forValidateRequest_whenRequestBodyIsNotValid() {
        ResponseEntity<String> responseEntity = postArticle(new Article(articleId, null, null), accessToken);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveHttpStatusBadRequest_forValidateRequest_whenArticleIdInPathVariableIsNotValid() {
        // articleId in pathVariable should be UUID.
        String temp = articleId;
        articleId = "invalid-article-id";
        ResponseEntity<String> responseEntity = postArticle(accessToken);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        articleId = temp;
    }

    @Test
    public void shouldReceiveHttpStatusBadRequest_forValidateRequest_whenRequestBodyIsNull() {
        ResponseEntity<String> responseEntity = postArticle(null, accessToken);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveHttpResponseBody_forGetValidateDetails() {
        when(mockNewsValidationService.getArticleValidationStatus(articleId))
                .thenReturn(getArticleValidationStatus());

        ResponseEntity<ArticleValidationStatus> responseEntity = get(articleId, accessToken);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(getArticleValidationStatus(), responseEntity.getBody());
    }
    @Test
    public void shouldReceiveBadRequest_forGetValidateDetails_whenArticleIdIsInvalid() {
        ResponseEntity<ArticleValidationStatus> responseEntity = get("invalid-article-id", accessToken);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveNotFound_forGetValidateDetails_whenArticleIdIsInvalid() {
        when(mockNewsValidationService.getArticleValidationStatus(articleId))
                .thenReturn(null);

        ResponseEntity<ArticleValidationStatus> responseEntity = get(articleId, accessToken);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveUnAuthorised_forGetValidateDetails_whenAccessTokenIsInvalid() {
        ResponseEntity<ArticleValidationStatus> responseEntity = get(articleId, invalidAccessToken);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldReceiveUnAuthorised_forGetValidateDetails_whenAccessTokenHasExpired() {
        ResponseEntity<ArticleValidationStatus> responseEntity = get(articleId, expiredAccessToken);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldInvokeValidationService_withGivenArticle_forValidateRequest() {
        postArticle(accessToken);
        verify(mockNewsValidationService).validate(article);
    }

    @Test
    public void shouldInvokeValidationService_withGivenArticleId_forGetValidateDetails() {
        get(articleId, accessToken);
        verify(mockNewsValidationService).getArticleValidationStatus(articleId);
    }

    private ResponseEntity<String> postArticle(String accessToken) {
        return postArticle(article, accessToken);
    }

    private ResponseEntity<String> postArticle(Article article, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Article> httpRequest = new HttpEntity<>(article,headers);

        return testRestTemplate.exchange(URI.create("http://localhost:8080"+"/news/validation/"+articleId),
                                                            HttpMethod.POST,
                                                            httpRequest,
                                                            String.class);
    }

    private ResponseEntity<ArticleValidationStatus> get(String articleId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpRequest = new HttpEntity(headers);

        return testRestTemplate.exchange(URI.create("http://localhost:8080"+"/news/validation/"+articleId),
                HttpMethod.GET,
                httpRequest,
                ArticleValidationStatus.class);
    }
//TODO:Make this generic exchange method for both get and post
//    private ResponseEntity<T> exchange(URI uri,
//                                       HttpMethod method,
//                                       HttpEntity<Article> httpRequest,
//                                       T responseObject) {
//        ResponseEntity<T> responseEntity = testRestTemplate.exchange(
//                uri,
//                method,
//                httpRequest,
//                responseObject.class);
//
//        return responseEntity;
//    }

}
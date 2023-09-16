package com.news.newsvalidationapi.controller;

import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.JwtRequest;
import com.news.newsvalidationapi.dto.JwtResponse;
import com.news.newsvalidationapi.service.NewsValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class NewsValidationControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JwtRequest jwtRequest;
    @MockBean
    private NewsValidationService mockNewsValidationService;

    private String accessToken;
    private String articleId="74b6a64c-5461-11ee-8c99-0242ac120002";
    private Article article;
    private final String invalidAccessToken = "invalidAccessToken";

    private final String expiredAccessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkxyZUNOYVNZNjFoUVFaZEZXemNlZCJ9." +
            "eyJpc3MiOiJodHRwczovL2Rldi1tbjdreWV2Nzg0YWMxdGoyLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJmcmc0OWlEOVZScjhHeHZUU3FjckRtUFJ" +
            "vSzk1Tm43b0BjbGllbnRzIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAwL25ld3MvdmFsaWRhdGlvbiIsImlhdCI6MTY5NDc3MzAwMSwiZX" +
            "hwIjoxNjk0ODU5NDAxLCJhenAiOiJmcmc0OWlEOVZScjhHeHZUU3FjckRtUFJvSzk1Tm43byIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyJ9." +
            "HQXc9ljPeGYL7bClzIiRlTFc15wEzsx2PDbIFHP58oBvpWtzFRXrlLI50pzu12XNZRtTyDnZx6gKXTkDndHXsPMCsfzalZCxuiBwpw3cMDiFMGr" +
            "Ub5EpSbqtJAQPR5B8Qau1wDzg6rPItURWOYNYmjNt2SNInrDdmI_mRe530LegwW-WWU97QCmSQIgnMFQsJyozmVzoHYXYTCH5zBn7GEkvWqLvtc" +
            "FYUyfe_pNWYSHj5gwDX7DEaCg50kANOwRd_Lma9BF8w4udjb2dosMdo44sCaUaJU3-_3_5FYz4UBzi3hZU-2R1J5tTalWF8Lehn17luedm4sLijEUjR943uQ";

    @BeforeAll
    public void setUp() {
        JwtResponse jwtResponse = testRestTemplate.postForObject(
                URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);

        accessToken = jwtResponse.getAccessToken();

        article = new Article(articleId,"test-article","test-article-text");
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

    }
    @Test
    public void shouldReceiveUnAuthorised_forGetValidateDetails_whenAccessTokenIsInvalid() {

    }

    @Test
    public void shouldReceiveUnAuthorised_forGetValidateDetails_whenAccessTokenHasExpired() {

    }

    @Test
    public void shouldInvokeValidationService_withGivenArticle_forValidateRequest() {
        postArticle(accessToken);
        verify(mockNewsValidationService).validate(article);
    }

    @Test
    public void shouldInvokeValidationService_withGivenArticleId_forGetValidateDetails() {

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
package com.news.newsvalidationapi;

import com.news.newsvalidationapi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class NewsValidationTestBase {
    @Autowired
    protected TestRestTemplate testRestTemplate;
    @Autowired
    protected JwtRequest jwtRequest;
    protected String accessToken;
    protected String articleId = "74b6a64c-5461-11ee-8c99-0242ac120002";
    protected Article article = new Article(articleId,"test-article","test-article-text");
    protected final String invalidAccessToken = "invalidAccessToken";
    protected final String expiredAccessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkxyZUNOYVNZNjFoUVFaZEZXemNlZCJ9." +
            "eyJpc3MiOiJodHRwczovL2Rldi1tbjdreWV2Nzg0YWMxdGoyLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJmcmc0OWlEOVZScjhHeHZUU3FjckRtUFJ" +
            "vSzk1Tm43b0BjbGllbnRzIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAwL25ld3MvdmFsaWRhdGlvbiIsImlhdCI6MTY5NDc3MzAwMSwiZX" +
            "hwIjoxNjk0ODU5NDAxLCJhenAiOiJmcmc0OWlEOVZScjhHeHZUU3FjckRtUFJvSzk1Tm43byIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyJ9." +
            "HQXc9ljPeGYL7bClzIiRlTFc15wEzsx2PDbIFHP58oBvpWtzFRXrlLI50pzu12XNZRtTyDnZx6gKXTkDndHXsPMCsfzalZCxuiBwpw3cMDiFMGr" +
            "Ub5EpSbqtJAQPR5B8Qau1wDzg6rPItURWOYNYmjNt2SNInrDdmI_mRe530LegwW-WWU97QCmSQIgnMFQsJyozmVzoHYXYTCH5zBn7GEkvWqLvtc" +
            "FYUyfe_pNWYSHj5gwDX7DEaCg50kANOwRd_Lma9BF8w4udjb2dosMdo44sCaUaJU3-_3_5FYz4UBzi3hZU-2R1J5tTalWF8Lehn17luedm4sLijEUjR943uQ";

    protected ArticleValidationStatus getArticleValidationStatus(ValidationStatus validationStatus) {
        return ArticleValidationStatus
                .builder()
                .articleId(articleId)
                .validationStatus(validationStatus)
                .build();
    }

    protected ArticleValidationStatus getArticleValidationStatus() {
        return getArticleValidationStatus(ValidationStatus.IN_REVIEW_LEGAL);
    }

    protected ValidationReport getValidationReport(ValidationStatus validationStatus) {
        return new ValidationReport("report-id", getArticleValidationStatus(validationStatus), "recommendation");
    }

    protected ValidationReport getValidationReport() {
        return getValidationReport(ValidationStatus.IN_REVIEW_LEGAL);
    }
}

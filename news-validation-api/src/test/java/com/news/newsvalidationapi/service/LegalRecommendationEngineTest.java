package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.NewsValidationTestBase;
import com.news.newsvalidationapi.ValidationReportMatcher;
import com.news.newsvalidationapi.dto.Article;
import com.news.newsvalidationapi.dto.ValidationReport;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
* Receive article and a callback.
* Validate article, callback and start async processing.
* Return true if article valid and async process started.
* Once Async process is done. Send report on callback.
* Processing logic : Random sleep between 5 and 10 secs. Then return report.
* recommendation to article text mapping :
*       by senior author -> review_needed_once,
*       by rookie author -> review_needed_multiple,
*       by expert author -> review_not_needed
* */
/*TODO tests problem.
*  injecting mock to engine causes validation tests to fail. hence using custom matcher for ValidationReport*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
class LegalRecommendationEngineTest extends NewsValidationTestBase {
    final int DELAY_ASYNC_CALLBACK = 10*1000;
    @Mock
    Consumer<ValidationReport> mockCallbackForValidationReport;
    @Autowired
    private LegalRecommendationEngine legalRecommendationEngine;

    @Test
    public void shouldReturnTrue_whenArticleIsValid() {
        assertTrue(legalRecommendationEngine.recommend(
                getArticle(),
                mockCallbackForValidationReport));
    }
    @Test
    public void shouldThrowException_whenArticleIsInValid() {
        assertThrows(ConstraintViolationException.class,
                () -> legalRecommendationEngine.recommend(
                new Article("invalid-article-id", null, null),
                mockCallbackForValidationReport));
    }
    @Test
    public void shouldThrowException_whenCallbackIsNull() {
        assertThrows(ConstraintViolationException.class,
                ()->legalRecommendationEngine.recommend(
                getArticle(),
                null));
    }
    @Test
    public void shouldStartAsyncProcessingOfArticle_andReturnTrueImmediately() {
        assertTimeout(Duration.ofSeconds(1),
                () -> assertTrue(legalRecommendationEngine.recommend(
                getArticle(),
                mockCallbackForValidationReport)));
    }
    @Test
    public void shouldInvokeCallback_whenAsyncProcessingIsDone() {
        legalRecommendationEngine.recommend(
                getArticle(),
                mockCallbackForValidationReport);
        verify(mockCallbackForValidationReport,
                after(DELAY_ASYNC_CALLBACK).times(1))
                .accept(any(ValidationReport.class));
    }
    @Test
    public void shouldPass_recommendationValue_review_needed_once_whenArticleText_hasBySeniorAuthor() {
        legalRecommendationEngine.recommend(
                getArticleByAuthorType("by senior author"),
                mockCallbackForValidationReport);

        verify(mockCallbackForValidationReport,
                after(DELAY_ASYNC_CALLBACK).times(1))
                .accept(argThat(
                        new ValidationReportMatcher(getValidationReport("review_needed_once"))));
    }
    @Test
    public void shouldPass_recommendationValue_review_needed_multiple_whenArticleText_hasByRookieAuthor() {
        legalRecommendationEngine.recommend(
                getArticleByAuthorType("by rookie author"),
                mockCallbackForValidationReport);

        verify(mockCallbackForValidationReport,
                after(DELAY_ASYNC_CALLBACK).times(1))
                .accept(argThat(
                        new ValidationReportMatcher(getValidationReport("review_needed_multiple"))));
    }
    @Test
    public void shouldPass_recommendationValue_review_not_needed_whenArticleText_hasByExpertAuthor() {
        legalRecommendationEngine.recommend(
                getArticleByAuthorType("by expert author"),
                mockCallbackForValidationReport);

        verify(mockCallbackForValidationReport,
                after(DELAY_ASYNC_CALLBACK).times(1))
                .accept(argThat(
                        new ValidationReportMatcher(getValidationReport("review_not_needed"))));
    }
}
package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.NewsValidationTestBase;
import com.news.newsvalidationapi.dto.ArticleValidationStatus;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/* NewsValidationService functionality
 * 1. convert received Article to ArticleValidationStatus and save it with status RECEIVED
 * 2. Send it to LegalRecommendationEngine for validation and update status to IN_REVIEW_LEGAL
 * 3. When callback received from engine. Create ValidationReport entry in DB and save report. Change status to FINISHED.
 * 4. Send ValidationReport to publisher-api.
 * 5. getValidationStatus from repository.*/
/*
 * TODO:
 *  Retry on shouldSendValidationReport_toPublisherAPI
 *  Repository not available.(Two repositories)
 *  Callback from engine Timeout
 *  LegalRecommendationEngine Not Available.
 *  GetArticleValidationStatus returns empty optional from repository*/

@ExtendWith(MockitoExtension.class)
class NewsValidationServiceTest extends NewsValidationTestBase {
    @Mock
    private LegalRecommendationEngine legalRecommendationEngine;
    @Mock
    private ValidationReportRepository validationReportRepository;
    @Mock
    private PublisherApiClient publisherApiClient;
    @InjectMocks
    private NewsValidationService newsValidationService;

    @Test
    public void shouldCreateArticleValidationStatusInRepository_whenValidateIsCalled(){
        ArticleValidationStatus articleValidationStatus = getArticleValidationStatus(ValidationStatus.RECEIVED);
        newsValidationService.validate(getArticle());
        verify(articleValidationStatusRepository,times(1)).save(articleValidationStatus);
    }
    @Test
    public void shouldSendArticleToLegalRecommendationEngine_whenValidateIsCalled(){
        newsValidationService.validate(getArticle());
        verify(legalRecommendationEngine,times(1))
                .recommend(getArticle(), newsValidationService.getRecommendationEngineCallback());
    }
    @Test
    public void shouldChangeStatusField_ofArticleValidationStatusInRepository_whenLegalRecommendationEngineHasAccepted(){
        when(legalRecommendationEngine
                .recommend(getArticle(), newsValidationService.getRecommendationEngineCallback()))
                .thenReturn(true);
        newsValidationService.validate(getArticle());
        verify(articleValidationStatusRepository,times(1))
                .updateArticleValidationStatus(articleId, ValidationStatus.IN_REVIEW_LEGAL);
    }
    @Test
    public void shouldCreateValidationReportInRepository_whenCallbackFromLegalRecommendationEngine(){
        when(validationReportRepository.findById(getValidationReport().getReportId()))
                .thenReturn(Optional.of(getValidationReport(ValidationStatus.FINISHED)));
        newsValidationService.getRecommendationEngineCallback().accept(getValidationReport());
        verify(validationReportRepository, times(1)).save(getValidationReport());
    }
    @Test
    public void shouldChangeStatusField_ofArticleValidationStatusInRepository_afterCallbackFromLegalRecommendationEngine(){
        when(validationReportRepository.findById(getValidationReport().getReportId()))
                .thenReturn(Optional.of(getValidationReport(ValidationStatus.FINISHED)));
        newsValidationService.validate(getArticle());
        newsValidationService.getRecommendationEngineCallback().accept(getValidationReport());
        verify(articleValidationStatusRepository, times(1))
                .updateArticleValidationStatus(articleId, ValidationStatus.FINISHED);
    }
    @Test
    public void shouldSendValidationReport_toPublisherAPI(){
        when(validationReportRepository.findById(getValidationReport().getReportId()))
                .thenReturn(Optional.of(getValidationReport(ValidationStatus.FINISHED)));
        newsValidationService.validate(getArticle());
        newsValidationService.getRecommendationEngineCallback().accept(getValidationReport(ValidationStatus.FINISHED));
        verify(publisherApiClient, times(1))
                .postValidationReport(getValidationReport(ValidationStatus.FINISHED));
    }
    @Test
    public void shouldReturnArticleValidationStatus_whenGetValidationStatusIsCalled(){
        ArticleValidationStatus articleValidationStatus = getArticleValidationStatus(ValidationStatus.FINISHED);
        when(articleValidationStatusRepository.findById(articleId))
                .thenReturn(Optional.of(articleValidationStatus));

        var expected = newsValidationService.getArticleValidationStatus(articleId);

        assertEquals(expected, articleValidationStatus);
    }
    @Test
    public void shouldInvokeRepositoryFindById_whenGetValidationStatusIsCalled(){
        ArticleValidationStatus articleValidationStatus = getArticleValidationStatus(ValidationStatus.FINISHED);
        when(articleValidationStatusRepository.findById(articleId))
                .thenReturn(Optional.of(articleValidationStatus));

        newsValidationService.getArticleValidationStatus(articleId);

        verify(articleValidationStatusRepository,times(1)).findById(articleId);
    }
}
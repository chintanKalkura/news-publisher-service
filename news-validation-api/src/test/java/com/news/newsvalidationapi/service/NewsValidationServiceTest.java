package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.NewsValidationTestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


/* NewsValidationService functionality
 * 1. convert received Article to ArticleValidationStatus and save it with status RECEIVED
 * 2. Send it to LegalRecommendationEngine for validation and update status to IN_REVIEW_LEGAL
 * 3. When callback received from engine. Create ValidationReport entry in DB and save report. Change status to FINISHED.
 * 4. Send ValidationReport to publisher-api.*/
/*
 * TODO:
 *  Retry on shouldSendValidationReport_toPublisherAPI
 *  Repository not available
 *  Callback from engine Timeout
 *  LegalRecommendationEngine Not Available.*/

@ExtendWith(MockitoExtension.class)
class NewsValidationServiceTest extends NewsValidationTestBase {
    @Mock
    private NewsValidationRepository newsValidationRepository;
    @InjectMocks
    private NewsValidationService newsValidationService;

    @Test
    public void shouldCreateArticleValidationStatusInRepository_whenValidateIsCalled(){
        when(newsValidationRepository.save(articleValidationStatus))
                .thenReturn(articleValidationStatus);

        newsValidationService.validate(article);

        verify(newsValidationRepository.save(articleValidationStatus), times(1));
    }
    @Test
    public void shouldSendArticleToLegalRecommendationEngine_whenValidateIsCalled(){

    }
    @Test
    public void shouldChangeStatusField_ofArticleValidationStatusInRepository_whenLegalRecommendationEngineHasAccepted(){

    }

    @Test
    public void shouldCreateValidationReportInRepository_whenCallbackFromLegalRecommendationEngine(){

    }

    @Test
    public void shouldChangeStatusField_ofArticleValidationStatusInRepository_whenCallbackFromLegalRecommendationEngine(){

    }

    @Test
    public void shouldSendValidationReport_toPublisherAPI(){

    }


}
package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.NewsValidationTestBase;
import com.news.newsvalidationapi.dto.ValidationReport;
import com.news.newsvalidationapi.dto.ValidationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.Mockito.*;
/*
* TODO: exception handling and negative scenarios.*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
class PublisherApiClientTest extends NewsValidationTestBase {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PublisherApiDetails publisherApiDetails;
    @InjectMocks
    private PublisherApiClient publisherApiClient;

    @Test
    public void shouldPostTheValidationReportToPublisherApi() {
        when(publisherApiDetails.getBaseUrl()).thenReturn("http://localhost:3002");
        when(publisherApiDetails.getResourceUrl()).thenReturn("/news/publish/validation/");
        ValidationReport validationReport = getValidationReport(ValidationStatus.FINISHED);

        when(restTemplate.postForEntity(URI.create("http://localhost:3002"+"/news/publish/validation/"+articleId),
                                        validationReport,
                                        ResponseEntity.class))
                         .thenReturn(ResponseEntity.accepted().build());

        publisherApiClient.postValidationReport(validationReport);

        verify(restTemplate, times(1)).postForEntity(URI.create("http://localhost:3002"+"/news/publish/validation/"+articleId),
                                            validationReport,
                                            ResponseEntity.class);
    }

}
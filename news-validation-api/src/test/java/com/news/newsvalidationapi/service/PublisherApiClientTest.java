package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.NewsValidationTestBase;
import com.news.newsvalidationapi.dto.ValidationReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.Mockito.*;
/*
* TODO: exception handling for negative scenarios.
*  Retry scenario.*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockitoExtension.class)
class PublisherApiClientTest extends NewsValidationTestBase {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PublisherApiDetails publisherApiDetails;
    @Mock
    private AccessTokenProvider accessTokenProvider;
    @InjectMocks
    private PublisherApiClient publisherApiClient;

    @Test
    public void shouldPostTheValidationReportToPublisherApi() {
        when(publisherApiDetails.getBaseUrl()).thenReturn("http://localhost:3002");
        when(publisherApiDetails.getResourceUrl()).thenReturn("/news/publish/validation/article/");
        when(accessTokenProvider.getBearerJwt()).thenReturn("valid-access-token");

        ValidationReport validationReport = getValidationReport();
        HttpEntity<ValidationReport> httpRequest = getHttpEntity(validationReport);
        when(restTemplate.exchange(URI.create("http://localhost:3002"+"/news/publish/validation/article/"+articleId),
                                        HttpMethod.POST,
                                        httpRequest,
                                        HttpStatus.class))
                         .thenReturn(ResponseEntity.accepted().body(HttpStatus.ACCEPTED));

        publisherApiClient.postValidationReport(validationReport);

        verify(restTemplate, times(1))
                .exchange(URI.create("http://localhost:3002"+"/news/publish/validation/article/"+articleId),
                        HttpMethod.POST,
                        httpRequest,
                        HttpStatus.class);
    }

    @Test
    public void shouldHandleTheException_whenRestTemplateThrowsError() {
        when(publisherApiDetails.getBaseUrl()).thenReturn("http://localhost:3002");
        when(publisherApiDetails.getResourceUrl()).thenReturn("/news/publish/validation/article/");
        when(accessTokenProvider.getBearerJwt()).thenReturn("valid-access-token");

        ValidationReport validationReport = getValidationReport();
        HttpEntity<ValidationReport> httpRequest = getHttpEntity(validationReport);
        when(restTemplate.exchange(URI.create("http://localhost:3002"+"/news/publish/validation/article/"+articleId),
                HttpMethod.POST,
                httpRequest,
                HttpStatus.class))
                .thenThrow(new RestClientException("Exception!!!"));

        publisherApiClient.postValidationReport(validationReport);

        verify(restTemplate, times(1))
                .exchange(URI.create("http://localhost:3002"+"/news/publish/validation/article/"+articleId),
                        HttpMethod.POST,
                        httpRequest,
                        HttpStatus.class);
    }

    private static HttpEntity<ValidationReport> getHttpEntity(ValidationReport validationReport) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("valid-access-token");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(validationReport,headers);
    }

}
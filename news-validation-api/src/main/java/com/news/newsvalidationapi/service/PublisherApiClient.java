package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class PublisherApiClient {
    //TODO Research on RestTemplate and other HttpClient or RestClients.
    private static final Logger LOGGER = LogManager.getLogger(PublisherApiClient.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AccessTokenProvider accessTokenProvider;
    @Autowired
    private PublisherApiDetails publisherApiDetails;

    public void postValidationReport(ValidationReport validationReport) {
        LOGGER.info("Posting ValidationReport: {} to PublisherApi: {}", validationReport, publisherApiDetails);
        String url = publisherApiDetails.getBaseUrl()+publisherApiDetails.getResourceUrl()+validationReport.getArticleId();
        try {
            ResponseEntity<HttpStatus> responseEntity = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.POST,
                    getHttpRequest(validationReport),
                    HttpStatus.class);
            LOGGER.info("Received HttpStatus: {} from PublisherApi", responseEntity.getBody());
        }
        catch(Exception ex) {
            LOGGER.error(ex);
        }
    }

    private HttpEntity<ValidationReport> getHttpRequest(ValidationReport validationReport) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenProvider.getBearerJwt());
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(validationReport,headers);
    }
}

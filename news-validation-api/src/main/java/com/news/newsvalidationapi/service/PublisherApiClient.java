package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private PublisherApiDetails publisherApiDetails;

    public void postValidationReport(ValidationReport validationReport) {
        LOGGER.info("Posting ValidationReport: {} to PublisherApi: {}", validationReport, publisherApiDetails);
        String url = publisherApiDetails.getBaseUrl()+
                publisherApiDetails.getResourceUrl()+
                validationReport.getValidationStatus().getArticleId();

        ResponseEntity<HttpStatus> responseEntity = restTemplate.postForEntity(
                                                            URI.create(url),
                                                            validationReport,
                                                            HttpStatus.class);
        LOGGER.info("Received HttpStatus: {} from PublisherApi", responseEntity.getBody());
    }
}

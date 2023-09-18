package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.ValidationReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class PublisherApiClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PublisherApiDetails publisherApiDetails;

    public void postValidationReport(ValidationReport validationReport) {
        String url = publisherApiDetails.getBaseUrl()+
                publisherApiDetails.getResourceUrl()+
                validationReport.getValidationStatus().getArticleId();

        restTemplate.postForEntity(
                URI.create(url),
                validationReport,
                ResponseEntity.class);
    }
}

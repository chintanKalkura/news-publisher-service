package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.JwtRequest;
import com.news.newsvalidationapi.dto.JwtResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;

@Component
public class AccessTokenProvider {
    /*TODO Research on RestTemplate and other HttpClient or RestClients.
     * retry scenario.
     * Exception handling for negative scenarios.*/
    private static final Logger LOGGER = LogManager.getLogger(PublisherApiClient.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtRequest jwtRequest;
    private String accessToken;
    private LocalDateTime expiryTime;
    public String getBearerJwt() {
        if(accessToken == null || accessToken.isEmpty() || accessTokenHasExpired()) {
            LOGGER.info("Posting request to fetch access token from server.");
            try {
                JwtResponse jwtResponse = restTemplate.postForObject(
                        URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                        jwtRequest,
                        JwtResponse.class);

                this.accessToken = jwtResponse.getAccessToken();
                this.expiryTime = LocalDateTime.now().plusSeconds(Long.parseLong(jwtResponse.getExpiresIn()));

                LOGGER.info("Received access token from server. Access Token expiry at : {}", expiryTime);
            }
            catch(Exception ex) {
                LOGGER.error(ex);
            }
        }
        LOGGER.info("Returning saved access token. Access Token expiry at : {}",expiryTime);
        return this.accessToken;
    }

    private boolean accessTokenHasExpired() {
       return LocalDateTime.now().isAfter(expiryTime);
    }
}

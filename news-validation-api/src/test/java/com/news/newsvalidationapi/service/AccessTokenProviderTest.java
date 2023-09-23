package com.news.newsvalidationapi.service;

import com.news.newsvalidationapi.dto.JwtRequest;
import com.news.newsvalidationapi.dto.JwtResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessTokenProviderTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JwtRequest jwtRequest;
    @InjectMocks
    private AccessTokenProvider accessTokenProvider;

    @Test
    public void shouldGetAccessTokenFromServer_whenAccessTokenIsEmpty() {
        when(restTemplate.postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class))
                .thenReturn(getJwtResponse());
        var actual = accessTokenProvider.getBearerJwt();
        verify(restTemplate, times(1)).postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);
        assertEquals("valid-access-token", actual);
    }

    @Test
    public void shouldGetAccessTokenFromServer_whenAccessTokenIsExpired() {
        when(restTemplate.postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class))
                .thenReturn(getJwtResponse("0"));
        var actual1 = accessTokenProvider.getBearerJwt();
        var actual2 = accessTokenProvider.getBearerJwt();
        verify(restTemplate, times(2)).postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);
        assertEquals("valid-access-token", actual1);
        assertEquals("valid-access-token", actual2);
    }
    @Test
    public void shouldReturnSameAccessToken_whenAccessTokenIsValid() {
        when(restTemplate.postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class))
                .thenReturn(getJwtResponse());
        var actual1 = accessTokenProvider.getBearerJwt();
        var actual2 = accessTokenProvider.getBearerJwt();
        verify(restTemplate, times(1)).postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);
        assertEquals("valid-access-token", actual1);
        assertEquals("valid-access-token", actual2);
    }

    @Test
    public void shouldHandleException() {
        when(restTemplate.postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class))
                .thenThrow(new RestClientException("Exception!!!"));
        accessTokenProvider.getBearerJwt();
        verify(restTemplate, times(1)).postForObject(URI.create("https://dev-mn7kyev784ac1tj2.us.auth0.com/oauth/token"),
                jwtRequest,
                JwtResponse.class);
    }

    private JwtResponse getJwtResponse() {
        return getJwtResponse("86400");
    }

    private JwtResponse getJwtResponse(String expiry) {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("valid-access-token");
        jwtResponse.setExpiresIn(expiry);
        jwtResponse.setTokenType("Bearer");

        return jwtResponse;

    }
}
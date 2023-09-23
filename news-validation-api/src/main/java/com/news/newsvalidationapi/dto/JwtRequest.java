package com.news.newsvalidationapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtRequest {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("audience")
    private String audience;
    @JsonProperty("grant_type")
    private String grantType;
}

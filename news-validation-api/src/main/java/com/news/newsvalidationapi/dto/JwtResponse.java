package com.news.newsvalidationapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtResponse {
    @JsonProperty("access_token")
    private String AccessToken;
    @JsonProperty("expires_in")
    private String ExpiresIn;
    @JsonProperty("token_type")
    private String TokenType;
}

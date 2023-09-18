package com.news.newsvalidationapi;

import com.news.newsvalidationapi.dto.JwtRequest;
import com.news.newsvalidationapi.service.PublisherApiDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomisedWebSecurityConfiguration {
    @Value("${spring.security.oauth2.resource.server.jwt.issuer-uri}")
    private String issuerUrl;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (requests) -> requests
                        .anyRequest()
                        .authenticated()
        );
        http.csrf(csrf->
                csrf.disable()
        );
        http.oauth2ResourceServer(
                resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults())
        );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withIssuerLocation(issuerUrl)
                .build();
    }
    @Bean
    @ConfigurationProperties("jwt.request")
    public JwtRequest jwtRequest() {
        return new JwtRequest();
    }

    @Bean
    @ConfigurationProperties("news.publisher-api.server")
    public PublisherApiDetails publisherApiDetails() {
        return new PublisherApiDetails();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

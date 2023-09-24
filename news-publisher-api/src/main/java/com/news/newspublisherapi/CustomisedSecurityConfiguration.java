package com.news.newspublisherapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomisedSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (requests) -> requests
                        .anyRequest().authenticated()
        );
        http.csrf(csrf->
                csrf.disable()
        );
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails author = User.withUsername("author")
                .password(encoder.encode("AuthorPassword"))
                .roles("AUTHOR","READER")
                .build();
        UserDetails editor = User.withUsername("editor")
                .password(encoder.encode("EditorPassword"))
                .roles("EDITOR","AUTHOR","READER")
                .build();
        UserDetails reader = User.withUsername("reader")
                .password(encoder.encode("ReaderPassword"))
                .roles("READER")
                .build();
        return new InMemoryUserDetailsManager(author, editor, reader);
    }
}

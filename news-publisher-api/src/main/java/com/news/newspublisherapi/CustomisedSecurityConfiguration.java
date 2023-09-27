package com.news.newspublisherapi;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableScheduling
public class CustomisedSecurityConfiguration {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (requests) -> requests
                        .requestMatchers(HttpMethod.GET,"news/publish/article/status/**").hasAnyRole("READER","AUTHOR","EDITOR")
                        .requestMatchers(HttpMethod.GET, "/news/publish/article/**").hasAnyRole("AUTHOR","EDITOR")
                        .requestMatchers(HttpMethod.POST, "news/publish/article/**").hasAnyRole("AUTHOR")
                        .requestMatchers(HttpMethod.POST, "news/publish/article/**/recommend").hasAnyRole("EDITOR")
                        .requestMatchers(HttpMethod.PUT, "news/publish/article/**").hasAnyRole("AUTHOR")
                        .requestMatchers(HttpMethod.PATCH, "news/publish/article/**/status/**").hasAnyRole("EDITOR")
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

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("publishedArticles")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}

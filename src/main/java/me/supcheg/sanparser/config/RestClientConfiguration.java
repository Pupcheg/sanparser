package me.supcheg.sanparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Bean
    RestClient webClient() {
        return RestClient.builder()
                .messageConverters(messageConverters -> messageConverters
                        .add(new FormHttpMessageConverter())
                )
                .build();
    }
}

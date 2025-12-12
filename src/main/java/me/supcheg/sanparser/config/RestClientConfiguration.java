package me.supcheg.sanparser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
class RestClientConfiguration {
    @Bean
    RestClient webClient(
            @Value("${client.cookie}") String cookie
    ) {
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> httpHeaders
                        .add("Cookie", cookie)
                )
                .configureMessageConverters(messageConverters -> messageConverters
                        .withXmlConverter(new FormHttpMessageConverter())
                )
                .build();
    }
}

package me.supcheg.sanparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrentConfiguration {
    @Bean
    ExecutorService executor() {
        return Executors.newFixedThreadPool(6);
    }
}

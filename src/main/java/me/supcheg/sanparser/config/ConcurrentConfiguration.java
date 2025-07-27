package me.supcheg.sanparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class ConcurrentConfiguration {
    @Bean
    ExecutorService executor(int parallelism) {
        return Executors.newFixedThreadPool(parallelism);
    }

    @Bean
    int parallelism() {
        return Runtime.getRuntime().availableProcessors();
    }
}

package me.supcheg.sanparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

@Configuration
public class DataBufferConfiguration {
    @Bean
    DataBufferFactory dataBufferFactory() {
        return new DefaultDataBufferFactory();
    }
}

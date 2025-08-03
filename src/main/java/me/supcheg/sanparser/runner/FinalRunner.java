package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order // last
class FinalRunner implements ApplicationRunner {
    private final ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) {
        SpringApplication.exit(context, () -> 0);
    }
}
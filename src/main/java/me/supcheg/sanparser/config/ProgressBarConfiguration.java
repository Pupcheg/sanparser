package me.supcheg.sanparser.config;

import me.supcheg.sanparser.progress.ProgressBarFactory;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProgressBarConfiguration {
    @Bean
    ProgressBarFactory progressBarFactory(
            SantechItemSource source
    ) {
        return title ->
                new ProgressBarBuilder()
                        .setTaskName(title)
                        .setInitialMax(source.rootSize())
                        .setStyle(ProgressBarStyle.ASCII)
                        .showSpeed()
                        .setConsumer(new ConsoleProgressBarConsumer(System.out))
                        .build();
    }
}

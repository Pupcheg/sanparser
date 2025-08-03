package me.supcheg.sanparser.config;

import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.stream.Collectors;

@Configuration
class ProgressBarConfiguration {
    @Bean
    @Scope("prototype")
    ProgressBar progressBar(
            String progressBarTitle,
            SantechItemSource source
    ) {
        return new ProgressBarBuilder()
                .setTaskName(progressBarTitle)
                .setInitialMax(source.rootSize())
                .setStyle(ProgressBarStyle.ASCII)
                .showSpeed()
                .setConsumer(new ConsoleProgressBarConsumer(System.out))
                .build();
    }

    @Bean
    String progressBarTitle(ApplicationArguments args) {
        return args.getOptionValues("mode")
                .stream()
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(","));
    }
}

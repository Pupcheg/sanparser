package me.supcheg.sanparser.download.recognizer.web;

import me.supcheg.sanparser.delay.DelayFactory;
import me.supcheg.sanparser.download.recognizer.DelayedUriRecognizer;
import me.supcheg.sanparser.download.recognizer.UriRecognizer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class WebUriRecognizerConfiguration {
    @Bean
    UriRecognizer webUriRecognizer(
            ApplicationArguments args,
            RestClient client,
            DelayFactory delayFactory
    ) {
        UriRecognizer webUriRecognizer = new WebUriRecognizer(client);

        if (!args.getOptionValues("download-mode").contains("no-delay")) {
            webUriRecognizer = new DelayedUriRecognizer(webUriRecognizer, delayFactory);
        }

        return webUriRecognizer;
    }
}

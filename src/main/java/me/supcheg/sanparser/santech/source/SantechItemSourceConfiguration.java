package me.supcheg.sanparser.santech.source;

import me.supcheg.sanparser.document.DocumentParser;
import me.supcheg.sanparser.download.UriDownloader;
import me.supcheg.sanparser.santech.repository.SantechItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
class SantechItemSourceConfiguration {
    @Bean
    SantechItemSource santechItemSource(
            ApplicationArguments args,
            UriDownloader uriDownloader,
            DocumentParser documentParser,
            SantechItemRepository itemRepository,
            @Value("${santech.catalog-uri}") URI catalog
    ) {
        SantechItemSource santechItemSource = new DefaultSantechItemSource(
                uriDownloader,
                documentParser,
                itemRepository,
                catalog
        );

        if (args.containsOption("items-limit")) {
            santechItemSource = new LimitedSantechItemSource(
                    santechItemSource,
                    Integer.parseInt(args.getOptionValues("items-limit").getFirst())
            );
        }

        return santechItemSource;
    }
}

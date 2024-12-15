package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class ParsingSantechUriSource implements SantechUriSource {
    private final UriParser uriParser;
    @Value("${santech.catalog-uri}")
    private URI catalog;

    @Override
    public Flux<URI> uris() {
        return Mono.just(catalog)
                .flatMap(uriParser::parse)
                .flatMapMany(document ->
                        Flux.fromIterable(document.getAllElements())
                                .filter(element -> element.tagName().equalsIgnoreCase("url"))
                                .map(element -> URI.create(element.ownText().strip()))
                                .take(10)
                );
    }

}

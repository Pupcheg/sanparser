package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class ParsingSantechUriSource implements SantechUriSource {
    private final UriParser uriParser;
    @Value("${santech.catalog-uri}")
    private URI catalog;

    @Override
    public Stream<URI> uris() {
        return uriParser.parse(catalog)
                .map(document ->
                        document.getAllElements().stream()
                                .filter(element -> element.tagName().equalsIgnoreCase("url"))
                                .map(element -> URI.create(element.ownText().strip()))
                )
                .orElseGet(Stream::empty);
    }

    @Override
    public long size() {
        return uris().count();
    }

}

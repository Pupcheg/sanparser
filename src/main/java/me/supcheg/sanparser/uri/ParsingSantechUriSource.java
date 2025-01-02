package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class ParsingSantechUriSource implements SantechUriSource {
    private final UriParser uriParser;
    @Value("${santech.catalog-uri}")
    private URI catalog;

    @Override
    public Stream<URI> uris() {
        return Stream.of(catalog)
                .map(uriParser::parse)
                .<Document>mapMulti(Optional::ifPresent)
                .flatMap(Document::stream)
                .filter(element -> element.tagName().equalsIgnoreCase("url"))
                .map(element -> URI.create(element.ownText().strip()));
    }

    @Override
    public long size() {
        return uris().count();
    }

}

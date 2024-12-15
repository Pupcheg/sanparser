package me.supcheg.sanparser.uri;

import org.jsoup.nodes.Document;
import reactor.core.publisher.Mono;

import java.net.URI;

public interface UriParser {
    Mono<Document> parse(URI uri);
}

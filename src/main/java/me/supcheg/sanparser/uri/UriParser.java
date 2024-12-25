package me.supcheg.sanparser.uri;

import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.Optional;

public interface UriParser {
    Optional<Document> parse(URI uri);
}

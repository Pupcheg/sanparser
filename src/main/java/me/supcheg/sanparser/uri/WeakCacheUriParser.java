package me.supcheg.sanparser.uri;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Primary
public class WeakCacheUriParser implements UriParser {
    private final UriParser delegate;
    private final Cache<URI, Document> cache = CacheBuilder.newBuilder().weakValues().build();

    @Override
    public Optional<Document> parse(URI uri) {
        return getDocumentFromCache(uri)
                .or(() ->
                        delegate.parse(uri)
                                .map(document -> putDocumentToCache(uri, document))
                );
    }

    private Optional<Document> getDocumentFromCache(URI key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    private Document putDocumentToCache(URI uri, Document document) {
        cache.put(uri, document);
        return document;
    }
}

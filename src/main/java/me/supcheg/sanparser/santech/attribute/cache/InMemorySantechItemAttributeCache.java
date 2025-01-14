package me.supcheg.sanparser.santech.attribute.cache;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Order(0)
@Component
class InMemorySantechItemAttributeCache implements ListeningSantechItemAttributeCache {
    private final Map<String, Map<URI, Optional<?>>> cache = new ConcurrentHashMap<>();
    private final SantechItemAttribute<URI> uri;

    @Override
    public <T> Optional<Optional<T>> find(CacheableSantechItemAttribute<T> attribute, SantechItem item) {
        return item.attribute(uri)
                .map(uri -> {
                    Map<URI, Optional<T>> attributeCache = attributeCache(attribute.key());
                    return attributeCache.get(uri);
                });
    }

    @Override
    public <T> void save(CacheableSantechItemAttribute<T> attribute, SantechItem item, Optional<T> value) {
        item.attribute(uri)
                .ifPresent(uri -> {
                    Map<URI, Optional<T>> attributeCache = attributeCache(attribute.key());
                    attributeCache.put(uri, value);
                });
    }

    private <T> Map<URI, Optional<T>> attributeCache(String attribute) {
        return (Map<URI, Optional<T>>) (Object) cache.computeIfAbsent(attribute, __ -> new ConcurrentHashMap<>());
    }

    @Override
    public void accept(ProvidingSantechItemAttributeCache.Entry entry) {
        attributeCache(entry.attribute())
                .put(entry.uri(), entry.value().flatMap(Optional::of));
    }
}

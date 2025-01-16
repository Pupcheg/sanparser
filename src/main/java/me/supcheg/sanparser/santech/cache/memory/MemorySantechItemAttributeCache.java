package me.supcheg.sanparser.santech.cache.memory;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;
import me.supcheg.sanparser.santech.cache.ListeningSantechItemAttributeCache;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Order(0)
@Component
class MemorySantechItemAttributeCache implements ListeningSantechItemAttributeCache {
    private final Map<CacheableSantechItemAttribute<?>, Map<URI, AttributeCacheEntry<?>>> cache = newMap();

    @Override
    public <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute) {
        @SuppressWarnings("unchecked")
        AttributeCacheEntry<T> entry = (AttributeCacheEntry<T>) attributeCache(attribute).get(item.uri());
        return Optional.ofNullable(entry);
    }

    @Override
    public void saveEntry(AttributeCacheEntry<?> entry) {
        attributeCache(entry.attribute()).put(entry.uri(), entry);
    }

    private Map<URI, AttributeCacheEntry<?>> attributeCache(CacheableSantechItemAttribute<?> attribute) {
        return cache.computeIfAbsent(attribute, __ -> newMap());
    }

    @Override
    public void accept(Iterable<AttributeCacheEntry<?>> entries) {
        var collected = StreamSupport.stream(entries.spliterator(), false)
                .collect(
                        groupingBy(
                                AttributeCacheEntry::attribute,
                                this::newMap,
                                groupingBy(
                                        AttributeCacheEntry::uri,
                                        this::newMap,
                                        collectingAndThen(Collectors.toSet(), Iterables::getOnlyElement)
                                )
                        )
                );
        cache.putAll(collected);
    }

    private <K, V> Map<K, V> newMap() {
        return new ConcurrentHashMap<>();
    }
}

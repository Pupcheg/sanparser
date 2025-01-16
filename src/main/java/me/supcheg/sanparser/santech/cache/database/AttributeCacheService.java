package me.supcheg.sanparser.santech.cache.database;

import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;

import java.util.Optional;
import java.util.stream.Stream;

public interface AttributeCacheService {
    <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute);

    void saveEntry(AttributeCacheEntry<?> entry);

    Stream<AttributeCacheEntry<?>> findAllEntries();
}

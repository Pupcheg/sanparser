package me.supcheg.sanparser.santech.cache;

import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;

import java.net.URI;
import java.util.Optional;

public record AttributeCacheEntry<T>(
        URI uri,
        CacheableSantechItemAttribute<T> attribute,
        Optional<T> value
) {
}

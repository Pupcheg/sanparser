package me.supcheg.sanparser.santech.cache;

import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;

import java.util.Optional;

public interface SantechItemAttributeCache {
    <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute);

    void saveEntry(AttributeCacheEntry<?> entry);
}

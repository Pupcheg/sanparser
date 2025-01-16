package me.supcheg.sanparser.santech.cache.database;

import me.supcheg.sanparser.data.attribute.AttributeCache;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;

public interface AttributeCacheConverter {
    AttributeCache convert(AttributeCacheEntry<?> entry);

    <T> AttributeCacheEntry<T> convert(AttributeCache entity);
}

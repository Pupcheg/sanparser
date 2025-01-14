package me.supcheg.sanparser.santech.attribute.cache;

import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;

import java.util.Optional;

public interface SantechItemAttributeCache {
    <T> Optional<Optional<T>> find(CacheableSantechItemAttribute<T> attribute, SantechItem item);

    <T> void save(CacheableSantechItemAttribute<T> attribute, SantechItem item, Optional<T> value);
}

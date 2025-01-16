package me.supcheg.sanparser.santech.cache;

import java.util.stream.Stream;

public interface ProvidingSantechItemAttributeCache extends SantechItemAttributeCache {
    Stream<AttributeCacheEntry<?>> entries();
}

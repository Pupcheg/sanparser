package me.supcheg.sanparser.santech.cache;

import java.util.Collection;

public interface ListeningSantechItemAttributeCache extends SantechItemAttributeCache {
   void accept(Collection<AttributeCacheEntry<?>> entries);
}

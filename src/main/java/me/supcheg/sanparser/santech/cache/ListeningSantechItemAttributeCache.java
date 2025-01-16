package me.supcheg.sanparser.santech.cache;

public interface ListeningSantechItemAttributeCache extends SantechItemAttributeCache {
   void accept(Iterable<AttributeCacheEntry<?>> entries);
}

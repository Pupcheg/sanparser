package me.supcheg.sanparser.santech.attribute.cache;

public interface ListeningSantechItemAttributeCache extends SantechItemAttributeCache {
    void accept(ProvidingSantechItemAttributeCache.Entry entry);
}

package me.supcheg.sanparser.santech.cache.database;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.ProvidingSantechItemAttributeCache;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Order(1)
@Component
class DatabaseSantechItemAttributeCache implements ProvidingSantechItemAttributeCache {
    private final AttributeCacheService attributeCacheService;

    @Override
    public <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute) {
        return attributeCacheService.findEntry(item, attribute);
    }

    @Override
    public void saveEntry(AttributeCacheEntry<?> entry) {
        attributeCacheService.saveEntry(entry);
    }

    @Override
    public Stream<AttributeCacheEntry<?>> entries() {
        return attributeCacheService.findAllEntries();
    }
}

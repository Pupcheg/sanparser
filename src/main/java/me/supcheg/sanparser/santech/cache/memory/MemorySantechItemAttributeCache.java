package me.supcheg.sanparser.santech.cache.memory;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;
import me.supcheg.sanparser.santech.cache.ListeningSantechItemAttributeCache;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Tables.toTable;
import static java.util.function.UnaryOperator.identity;

@RequiredArgsConstructor
@Order(0)
@Component
class MemorySantechItemAttributeCache implements ListeningSantechItemAttributeCache {
    private Table<CacheableSantechItemAttribute<?>, URI, AttributeCacheEntry<?>> cache = ImmutableTable.of();

    @Override
    public <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute) {
        @SuppressWarnings("unchecked")
        AttributeCacheEntry<T> entry = (AttributeCacheEntry<T>) cache.get(attribute, item.uri());
        return Optional.ofNullable(entry);
    }

    @Override
    public void saveEntry(AttributeCacheEntry<?> entry) {
        cache.put(entry.attribute(), entry.uri(), entry);
    }

    @Override
    public void accept(Collection<AttributeCacheEntry<?>> entries) {
        cache = entries.stream()
                .collect(toTable(
                                AttributeCacheEntry::attribute,
                                AttributeCacheEntry::uri,
                                identity(),
                                () -> Tables.newCustomTable(new ConcurrentHashMap<>(), ConcurrentHashMap::new)
                        )
                );
    }
}

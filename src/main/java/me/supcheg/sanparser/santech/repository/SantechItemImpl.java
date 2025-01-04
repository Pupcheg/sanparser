package me.supcheg.sanparser.santech.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.net.URI;
import java.util.Optional;

public record SantechItemImpl(
        URI uri,
        Cache<SantechItemAttribute<?>, Optional<Object>> attributes
) implements SantechItem {

    public SantechItemImpl(URI uri) {
        this(
                uri,
                CacheBuilder.newBuilder().softValues().build()
        );
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public <T> Optional<T> attribute(SantechItemAttribute<T> attribute) {
        return (Optional<T>) attributes.get(attribute, () -> (Optional<Object>) attribute.find(this));
    }
}

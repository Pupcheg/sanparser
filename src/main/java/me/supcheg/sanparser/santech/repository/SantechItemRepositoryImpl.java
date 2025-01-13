package me.supcheg.sanparser.santech.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import me.supcheg.sanparser.santech.SantechItem;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
class SantechItemRepositoryImpl implements SantechItemRepository {
    private final Cache<URI, SantechItem> cache = CacheBuilder.newBuilder()
            .weakValues()
            .build();

    @SneakyThrows
    @Override
    public SantechItem item(URI uri) {
        return cache.get(uri, () -> new SantechItemImpl(uri));
    }
}

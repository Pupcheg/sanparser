package me.supcheg.sanparser.santech.attribute.cacheable;

import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cache.SantechItemAttributeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

@Slf4j
public abstract class CacheableSantechItemAttributeImpl<T> implements CacheableSantechItemAttribute<T> {

    @Lazy
    @Autowired
    private SantechItemAttributeCache cache;

    @Override
    public final Optional<T> find(SantechItem item) {
        return cache.find(this, item)
                .orElseGet(() -> {
                    Optional<T> value = findIternal(item);
                    cache.save(this, item, value);
                    return value;
                });
    }

    protected abstract Optional<T> findIternal(SantechItem item);
}

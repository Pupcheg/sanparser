package me.supcheg.sanparser.santech.cache;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor
@Component
class SequenceSantechItemAttributeCache implements SantechItemAttributeCache {
    private final List<SantechItemAttributeCache> delegates;

    @Autowired
    void bootstrap(
            List<ProvidingSantechItemAttributeCache> providing,
            List<ListeningSantechItemAttributeCache> listening
    ) {
        Set<AttributeCacheEntry<?>> entries = providing.stream()
                .flatMap(ProvidingSantechItemAttributeCache::entries)
                .collect(Collectors.toSet());

        listening.forEach(listener -> listener.accept(entries));
    }

    @Override
    public <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item, CacheableSantechItemAttribute<T> attribute) {
        return delegates.stream()
                .map(delegate -> delegate.findEntry(item, attribute))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(Optional::empty);
    }

    @Override
    public void saveEntry(AttributeCacheEntry<?> entry) {
        delegates.forEach(delegate -> delegate.saveEntry(entry));
    }
}

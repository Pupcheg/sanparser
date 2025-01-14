package me.supcheg.sanparser.santech.attribute.cache;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Primary
@RequiredArgsConstructor
@Component
public class SequenceSantechItemAttributeCache implements SantechItemAttributeCache {
    private final List<SantechItemAttributeCache> delegates;

    @Autowired
    void bootstrap(
            List<ListeningSantechItemAttributeCache> listening,
            List<ProvidingSantechItemAttributeCache> providing
    ) {
        providing.forEach(provider -> provider.entries()
                .forEach(entry -> listening.forEach(listener -> listener.accept(entry))));
    }

    @Override
    public <T> Optional<Optional<T>> find(CacheableSantechItemAttribute<T> attribute, SantechItem item) {
        return delegates.stream()
                .map(delegate -> delegate.find(attribute, item))
                .findFirst()
                .orElseGet(Optional::empty);
    }

    @Override
    public <T> void save(CacheableSantechItemAttribute<T> attribute, SantechItem item, Optional<T> value) {
        delegates.forEach(delegate -> delegate.save(attribute, item, value));
    }
}

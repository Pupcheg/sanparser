package me.supcheg.sanparser.santech.cache.database;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.attribute.AttributeRepository;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
class DefaultAttributeCacheService implements AttributeCacheService {
    private final AttributeRepository attributeCacheRepository;
    private final AttributeCacheConverter attributeCacheConverter;

    @Override
    public <T> Optional<AttributeCacheEntry<T>> findEntry(SantechItem item,
                                                          CacheableSantechItemAttribute<T> attribute) {
        return attributeCacheRepository.findByUriAndAttributeKey(item.uri(), attribute.key())
                .map(attributeCacheConverter::convert);
    }

    @Override
    public void saveEntry(AttributeCacheEntry<?> entry) {
        attributeCacheRepository.save(attributeCacheConverter.convert(entry));
    }

    @Override
    public Stream<AttributeCacheEntry<?>> findAllEntries() {
        return StreamSupport.stream(attributeCacheRepository.findAll().spliterator(), false)
                .map(attributeCacheConverter::convert);
    }
}

package me.supcheg.sanparser.santech.attribute.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.data.attribute.AttributeCache;
import me.supcheg.sanparser.data.attribute.AttributeRepository;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Order(1)
@Component
class DatabaseSantechItemAttributeCache implements ProvidingSantechItemAttributeCache {
    private final SantechItemAttribute<URI> uri;
    private final AttributeRepository attributeRepository;
    private final ObjectMapper objectMapper;

    private final Map<String, TypeReference<?>> typeReferenceByAttributeKey = new HashMap<>();

    @Autowired
    void buildTypeReferenceByAttributeKey(
            List<CacheableSantechItemAttribute<?>> attributes
    ) {
        typeReferenceByAttributeKey.putAll(
                attributes.stream()
                        .collect(toMap(SantechItemAttribute::key, CacheableSantechItemAttribute::type))
        );
    }

    @Override
    public <T> Optional<Optional<T>> find(CacheableSantechItemAttribute<T> attribute, SantechItem item) {
        return item.attribute(uri)
                .flatMap(uri -> attributeRepository.findByUriAndAttributeKey(uri, attribute.key()))
                .map(cache -> convertJsonValue(cache, attribute::type));
    }

    @SneakyThrows
    private <T> T convertFromString(String raw, TypeReference<T> type) {
        return objectMapper.readValue(raw, type);
    }

    @Override
    public <T> void save(CacheableSantechItemAttribute<T> attribute, SantechItem item, Optional<T> value) {
        item.attribute(uri)
                .ifPresent(uri ->
                        attributeRepository.save(
                                me.supcheg.sanparser.data.attribute.AttributeCache.builder()
                                        .uri(uri)
                                        .attributeKey(attribute.key())
                                        .jsonValue(value.map(this::convertToString).orElse(null))
                                        .build()
                        )
                );
    }

    @SneakyThrows
    private String convertToString(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public Stream<Entry> entries() {
        return StreamSupport.stream(attributeRepository.findAll().spliterator(), false)
                .map(this::convert);
    }

    private Entry convert(AttributeCache cache) {
        String key = cache.getAttributeKey();
        return new Entry(
                key,
                cache.getUri(),
                convertJsonValue(cache, () -> typeReferenceByAttributeKey.get(key))
        );
    }

    private <T> Optional<T> convertJsonValue(AttributeCache cache, Supplier<TypeReference<T>> type) {
        return Optional.ofNullable(cache.getJsonValue())
                .map(raw -> convertFromString(raw, type.get()));
    }
}

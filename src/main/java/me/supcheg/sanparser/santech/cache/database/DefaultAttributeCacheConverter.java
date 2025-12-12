package me.supcheg.sanparser.santech.cache.database;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.attribute.AttributeCache;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.cache.AttributeCacheEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.pivovarit.function.ThrowingFunction.sneaky;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Component
class DefaultAttributeCacheConverter implements AttributeCacheConverter {
    private final ObjectMapper objectMapper;
    private final Map<String, CacheableSantechItemAttribute<?>> itemAttributeByKey = new HashMap<>();

    @Autowired
    void buildItemAttributeByKey(
            List<CacheableSantechItemAttribute<?>> attributes
    ) {
        itemAttributeByKey.putAll(
                attributes.stream()
                        .collect(toMap(CacheableSantechItemAttribute::key, identity()))
        );
    }

    @Override
    public AttributeCache convert(AttributeCacheEntry<?> entry) {
        return AttributeCache.builder()
                .uri(entry.uri())
                .attributeKey(entry.attribute().key())
                .jsonValue(
                        entry.value()
                                .map(sneaky(objectMapper::writeValueAsString))
                                .orElse(null)
                )
                .build();
    }

    @Override
    public <T> AttributeCacheEntry<T> convert(AttributeCache entity) {
        URI uri = entity.getUri();
        CacheableSantechItemAttribute<T> attribute = attribute(entity.getAttributeKey());
        Optional<T> value = Optional.ofNullable(entity.getJsonValue())
                .map(sneaky(raw -> objectMapper.readValue(raw, attribute.type())));

        return new AttributeCacheEntry<>(uri, attribute, value);
    }


    private <T> CacheableSantechItemAttribute<T> attribute(String key) {
        CacheableSantechItemAttribute<?> attribute = itemAttributeByKey.get(key);
        if (attribute == null) {
            throw new IllegalArgumentException("No such attribute: " + key);
        }

        @SuppressWarnings("unchecked")
        CacheableSantechItemAttribute<T> cast = (CacheableSantechItemAttribute<T>) attribute;
        return cast;
    }
}

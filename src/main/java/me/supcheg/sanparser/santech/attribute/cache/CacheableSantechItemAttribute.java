package me.supcheg.sanparser.santech.attribute.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.data.attribute.AttributeCache;
import me.supcheg.sanparser.data.attribute.AttributeRepository;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Optional;

import static me.supcheg.sanparser.unchecked.GenericTypeResolver.obtainType;

@Slf4j
public abstract class CacheableSantechItemAttribute<T> implements SantechItemAttribute<T> {

    protected final Class<T> type;

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private SantechItemAttribute<URI> uri;
    @Autowired
    private ObjectMapper objectMapper;

    protected CacheableSantechItemAttribute() {
        this(obtainType());
    }

    protected CacheableSantechItemAttribute(Class<T> type) {
        this.type = type;
    }

    @Override
    public final Optional<T> find(SantechItem item) {
        return lookupCache(item)
                .or(() ->
                        findIternal(item)
                                .map(value -> {
                                    saveCache(item, value);
                                    return value;
                                })
                );
    }

    protected abstract Optional<T> findIternal(SantechItem item);

    private Optional<T> lookupCache(SantechItem item) {
        return item.attribute(this.uri)
                .flatMap(uri -> attributeRepository.findByUriAndAttributeKey(uri, key()))
                .map(AttributeCache::getJsonValue)
                .map(jsonValue -> objectMapper.convertValue(jsonValue, type));
    }

    private void saveCache(SantechItem item, T value) {
        item.attribute(this.uri)
                .ifPresent(uri ->
                        attributeRepository.save(
                                new AttributeCache(
                                        uri,
                                        key(),
                                        convertToString(value)
                                )
                        )
                );
    }

    @SneakyThrows
    private String convertToString(T value) {
        return objectMapper.writeValueAsString(value);
    }
}

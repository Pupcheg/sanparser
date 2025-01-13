package me.supcheg.sanparser.santech.attribute.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.data.attribute.AttributeCache;
import me.supcheg.sanparser.data.attribute.AttributeRepository;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Contract;

import java.net.URI;
import java.util.Optional;

import static me.supcheg.sanparser.unchecked.GenericTypeResolver.obtainType;

@Slf4j
public abstract class CacheableSantechItemAttributeImpl<T> implements CacheableSantechItemAttribute<T> {

    protected final Class<T> type;

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private SantechItemAttribute<URI> uri;
    @Autowired
    protected ObjectMapper objectMapper;

    @SafeVarargs
    protected CacheableSantechItemAttributeImpl(T... typeArray) {
        this(obtainType(typeArray));
    }

    protected CacheableSantechItemAttributeImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public final Optional<T> find(SantechItem item) {
        return lookupCache(item)
                .or(() -> saveCache(item, findIternal(item)))
                .orElseGet(Optional::empty);
    }

    protected abstract Optional<T> findIternal(SantechItem item);

    private Optional<Optional<T>> lookupCache(SantechItem item) {
        return item.attribute(this.uri)
                .flatMap(uri -> attributeRepository.findByUriAndAttributeKey(uri, key()))
                .map(cache ->
                        Optional.ofNullable(cache.getJsonValue())
                                .map(this::convertFromString)
                );
    }

    @Contract("_ -> param1")
    private Optional<Optional<T>> saveCache(SantechItem item, Optional<T> value) {
        item.attribute(this.uri)
                .ifPresent(uri ->
                        attributeRepository.save(
                                AttributeCache.builder()
                                        .uri(uri)
                                        .attributeKey(key())
                                        .jsonValue(value.map(this::convertToString).orElse(null))
                                        .build()
                        )
                );
        return Optional.of(value);
    }

    @SneakyThrows
    protected T convertFromString(String raw) {
        return objectMapper.readValue(raw, type);
    }

    @SneakyThrows
    private String convertToString(T value) {
        return objectMapper.writeValueAsString(value);
    }
}

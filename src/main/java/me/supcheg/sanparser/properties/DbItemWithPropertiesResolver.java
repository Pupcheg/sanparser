package me.supcheg.sanparser.properties;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.properties.UriProperties;
import me.supcheg.sanparser.data.properties.UriPropertiesRepository;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Primary
public class DbItemWithPropertiesResolver implements ItemWithPropertiesResolver {
    private final ItemWithPropertiesResolver delegate;
    private final SantechIdentifierResolver santechIdentifierResolver;
    private final UriPropertiesRepository uriPropertiesRepository;

    @Override
    public Optional<ItemWithProperties> resolveItemWithProperties(URI uri) {
        return santechIdentifierResolver.resolveSantechIdentifier(uri)
                .flatMap(identifier ->
                        uriPropertiesRepository.findById(uri)
                                .map(uriProperties ->
                                        new ItemWithProperties(identifier, uriProperties.getProperties())
                                )
                                .or(() ->
                                        delegate.resolveItemWithProperties(uri)
                                                .map(itemWithProperties -> {
                                                    var properties = new UriProperties(uri, itemWithProperties.properties());
                                                    uriPropertiesRepository.save(properties);
                                                    return itemWithProperties;
                                                })
                                )
                );
    }
}

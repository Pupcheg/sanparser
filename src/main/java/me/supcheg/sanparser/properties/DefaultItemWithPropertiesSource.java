package me.supcheg.sanparser.properties;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.SantechUriSource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class DefaultItemWithPropertiesSource implements ItemWithPropertySource{
    private final SantechUriSource uriSource;
    private final ItemWithPropertiesResolver itemWithPropertiesResolver;

    @Override
    public Stream<ItemWithProperties> items() {
        return uriSource.uris()
                .map(itemWithPropertiesResolver::resolveItemWithProperties)
                .mapMulti(Optional::ifPresent);
    }
}

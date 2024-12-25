package me.supcheg.sanparser.category;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.group.UriCategory;
import me.supcheg.sanparser.data.group.UriCategoryRepository;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Primary
@Component
public class DbItemWithCategoryResolver implements ItemWithCategoryResolver {
    private final ItemWithCategoryResolver delegate;
    private final SantechIdentifierResolver santechIdentifierResolver;
    private final UriCategoryRepository uriCategoryRepository;

    @Override
    public Optional<ItemWithCategory> resolveItemWithCategory(URI uri) {
        return santechIdentifierResolver.resolveSantechIdentifier(uri)
                .flatMap(identifier ->
                        uriCategoryRepository.findById(uri)
                                .map(uriProperties ->
                                        new ItemWithCategory(identifier, uriProperties.getCategory())
                                )
                                .or(() ->
                                        delegate.resolveItemWithCategory(uri)
                                                .map(itemWithProperties -> {
                                                    var properties = new UriCategory(uri, itemWithProperties.category());
                                                    uriCategoryRepository.save(properties);
                                                    return itemWithProperties;
                                                })
                                )
                );
    }
}

package me.supcheg.sanparser.category;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.SantechUriSource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class DefaultItemWithCategorySource implements ItemWithCategorySource {
    private final SantechUriSource santechUriSource;
    private final ItemWithCategoryResolver itemWithCategoryResolver;

    @Override
    public Stream<ItemWithCategory> items() {
        return santechUriSource.uris()
                .map(itemWithCategoryResolver::resolveItemWithCategory)
                .mapMulti(Optional::ifPresent);
    }
}

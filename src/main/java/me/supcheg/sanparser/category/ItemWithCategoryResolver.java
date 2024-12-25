package me.supcheg.sanparser.category;

import java.net.URI;
import java.util.Optional;

public interface ItemWithCategoryResolver {
    Optional<ItemWithCategory> resolveItemWithCategory(URI uri);
}

package me.supcheg.sanparser.properties;

import java.net.URI;
import java.util.Optional;

public interface ItemWithPropertiesResolver {
    Optional<ItemWithProperties> resolveItemWithProperties(URI uri);
}

package me.supcheg.sanparser.santech.attribute.cache;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProvidingSantechItemAttributeCache extends SantechItemAttributeCache {

    Stream<Entry> entries();

    record Entry(
            String attribute,
            URI uri,
            Optional<?> value
    ) {
    }
}

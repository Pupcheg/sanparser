package me.supcheg.sanparser.santech.repository;

import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@Component
class RootUriAttribute implements SantechItemAttribute<URI> {
    @Override
    public String key() {
        return "uri";
    }

    @Override
    public Optional<URI> find(SantechItem item) {
        if (item instanceof SantechItemImpl(URI uri)) {
            return Optional.of(uri);
        }
        throw new IllegalArgumentException("Unable to locate SantechItem uri from " + item);
    }
}

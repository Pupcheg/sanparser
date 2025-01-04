package me.supcheg.sanparser.santech.attribute;

import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.repository.SantechItemImpl;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@Component
class UriAttribute implements SantechItemAttribute<URI> {
    @Override
    public String key() {
        return "uri";
    }

    @Override
    public Optional<URI> find(SantechItem attributed) {
        SantechItemImpl impl = (SantechItemImpl) attributed;
        return Optional.of(impl.uri());
    }
}

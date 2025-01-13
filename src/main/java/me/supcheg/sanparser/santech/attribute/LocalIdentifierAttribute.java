package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.local.LocalIdentifierLookup;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class LocalIdentifierAttribute implements SantechItemAttribute<LocalIdentifier> {
    private final SantechItemAttribute<SantechIdentifier> santechIdentifier;
    private final LocalIdentifierLookup localIdentifierLookup;

    @Override
    public String key() {
        return "local_identifier";
    }

    @Override
    public Optional<LocalIdentifier> find(SantechItem item) {
        return item.attribute(santechIdentifier)
                .flatMap(localIdentifierLookup::findLocalIdentifier);
    }
}

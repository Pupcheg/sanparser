package me.supcheg.sanparser.association;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.id.SantechIdentifierSource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class DefaultAssociationsSource implements AssociationsSource {
    private final SantechIdentifierSource identifierSource;
    private final AssociationsResolver associationsResolver;

    @Override
    public Stream<AssociatedItem> associations() {
        return identifierSource.santechIdentifiers()
                .map(associationsResolver::resolveAssociations);
    }
}

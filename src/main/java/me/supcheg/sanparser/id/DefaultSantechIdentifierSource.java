package me.supcheg.sanparser.id;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.SantechUriSource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class DefaultSantechIdentifierSource implements SantechIdentifierSource {
    private final SantechUriSource uriSource;
    private final SantechIdentifierResolver santechIdentifierResolver;

    @Override
    public Stream<SantechIdentifier> santechIdentifiers() {
        return uriSource.uris()
                .map(santechIdentifierResolver::resolveSantechIdentifier)
                .mapMulti(Optional::ifPresent);
    }
}

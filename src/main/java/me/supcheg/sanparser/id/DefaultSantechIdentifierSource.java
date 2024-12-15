package me.supcheg.sanparser.id;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.SantechUriSource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class DefaultSantechIdentifierSource implements SantechIdentifierSource {
    private final SantechUriSource uriSource;
    private final SantechIdentifierResolver santechIdentifierResolver;

    @Override
    public Flux<SantechIdentifier> santechIdentifiers() {
        return uriSource.uris()
                .flatMap(santechIdentifierResolver::resolveSantechIdentifier);
    }
}

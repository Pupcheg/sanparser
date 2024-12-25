package me.supcheg.sanparser.id;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.uri.IdentifiedUri;
import me.supcheg.sanparser.data.uri.IdentifiedUriRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Primary
@Component
public class DbSantechIdentifierResolver implements SantechIdentifierResolver {
    private final SantechIdentifierResolver delegate;
    private final IdentifiedUriRepository urlRepository;

    @Override
    public Optional<SantechIdentifier> resolveSantechIdentifier(URI uri) {
        return urlRepository.findById(uri)
                .map(IdentifiedUri::getSantechIdentifier)
                .or(
                        () -> delegate.resolveSantechIdentifier(uri)
                                .map(santechIdentifier -> {
                                    urlRepository.save(new IdentifiedUri(uri, santechIdentifier));
                                    return santechIdentifier;
                                })
                );
    }
}

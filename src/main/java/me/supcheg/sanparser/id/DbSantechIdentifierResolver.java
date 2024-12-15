package me.supcheg.sanparser.id;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.IdentifiedUri;
import me.supcheg.sanparser.data.IdentifiedUriMapper;
import me.supcheg.sanparser.data.IdentifiedUriRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@RequiredArgsConstructor
@Primary
@Component
public class DbSantechIdentifierResolver implements SantechIdentifierResolver {
    private final SantechIdentifierResolver delegate;
    private final R2dbcEntityOperations operations;
    private final IdentifiedUriRepository urlRepository;
    private final IdentifiedUriMapper identifiedUriMapper;

    @Override
    public Mono<SantechIdentifier> resolveSantechIdentifier(URI uri) {
        String rawUri = uri.toString();
        return urlRepository.findFirstByUri(rawUri)
                .switchIfEmpty(
                        delegate.resolveSantechIdentifier(uri)
                                .map(toIdentifiedUriWithUri(rawUri))
                                .flatMap(operations::insert)
                )
                .map(identifiedUriMapper::identifiedUrlToSantechIdentifier);
    }

    private Function<SantechIdentifier, IdentifiedUri> toIdentifiedUriWithUri(String uri) {
        return santechIdentifier -> {
            IdentifiedUri identifiedUri = identifiedUriMapper.santechIdentifierToIdentifiedUrl(santechIdentifier);
            identifiedUri.setUri(uri);
            return identifiedUri;
        };
    }
}

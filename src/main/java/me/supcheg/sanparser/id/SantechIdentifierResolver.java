package me.supcheg.sanparser.id;

import reactor.core.publisher.Mono;

import java.net.URI;

public interface SantechIdentifierResolver {
    Mono<SantechIdentifier> resolveSantechIdentifier(URI uri);
}

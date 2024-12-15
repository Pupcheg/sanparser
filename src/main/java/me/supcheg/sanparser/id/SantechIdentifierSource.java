package me.supcheg.sanparser.id;

import reactor.core.publisher.Flux;

public interface SantechIdentifierSource {
    Flux<SantechIdentifier> santechIdentifiers();
}

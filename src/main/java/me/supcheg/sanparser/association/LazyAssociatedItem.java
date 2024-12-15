package me.supcheg.sanparser.association;

import me.supcheg.sanparser.id.SantechIdentifier;
import reactor.core.publisher.Flux;

public record LazyAssociatedItem(
        SantechIdentifier identifier,
        Flux<SantechIdentifier> analogues,
        Flux<SantechIdentifier> variants
) {
}

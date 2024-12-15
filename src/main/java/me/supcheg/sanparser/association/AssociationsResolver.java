package me.supcheg.sanparser.association;

import reactor.core.publisher.Flux;

public interface AssociationsResolver {
    Flux<LazyAssociatedItem> associations();
}

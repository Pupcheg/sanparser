package me.supcheg.sanparser.uri;

import reactor.core.publisher.Flux;

import java.net.URI;

public interface SantechUriSource {
    Flux<URI> uris();
}

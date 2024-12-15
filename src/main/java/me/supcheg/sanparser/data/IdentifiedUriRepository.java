package me.supcheg.sanparser.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IdentifiedUriRepository extends ReactiveCrudRepository<IdentifiedUri, String> {
    Mono<IdentifiedUri> findFirstByUri(String uri);
}

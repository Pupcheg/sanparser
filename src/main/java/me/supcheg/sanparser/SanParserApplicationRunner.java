package me.supcheg.sanparser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.association.AssociatedItem;
import me.supcheg.sanparser.association.AssociationsResolver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Component
public class SanParserApplicationRunner implements ApplicationRunner {
    private final AssociationsResolver source;

    @Override
    public void run(ApplicationArguments args) {
        source.associations()
                .flatMap(lazyAssociatedItem ->
                        Flux.zip(
                                lazyAssociatedItem.analogues().collectList(),
                                lazyAssociatedItem.variants().collectList(),
                                (analogues, variants) ->
                                        new AssociatedItem(
                                                lazyAssociatedItem.identifier(),
                                                analogues,
                                                variants
                                        )
                        )
                )
                .subscribeOn(Schedulers.parallel())
                .subscribe(item -> log.info("{}", item));
    }
}

package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.lookup.SantechItemAttributeLookup;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.pivovarit.collectors.ParallelCollectors.parallel;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
        name = "mode",
        havingValue = "attributes"
)
class AttributesRunner implements ApplicationRunner {
    private final Executor executor;
    private final int parallelism;
    private final SantechItemSource source;
    private final SantechItemAttributeLookup lookup;

    @Override
    public void run(ApplicationArguments args) {
        var attributes = attributes(args);

        if (attributes.isEmpty()) {
            log.info("No attributes requested");
            return;
        }

        log.info(
                "Requested attributes: {}",
                attributes.stream()
                        .map(SantechItemAttribute::key)
                        .collect(joining(", "))
        );

        try (var bar = buildProgressBar()) {
            source.items()
                    .peek(item -> attributes.forEach(item::attribute))
                    .collect(parallel(i -> bar.step(), counting(), executor, parallelism))
                    .thenRun(() -> {
                        log.info("Done");
                        System.exit(0);
                    })
                    .join();
        }
    }

    private List<SantechItemAttribute<?>> attributes(ApplicationArguments args) {
        Set<String> options = args.getOptionNames();

        if (options.stream().anyMatch("all"::equalsIgnoreCase)) {
            return lookup.all();
        }

        if (options.stream().anyMatch("cacheable"::equalsIgnoreCase)) {
            return lookup.all().stream()
                    .filter(CacheableSantechItemAttribute.class::isInstance)
                    .toList();
        }

        List<SantechItemAttribute<?>> attributes = new ArrayList<>();
        for (String option : options) {
            lookup.find(option)
                    .ifPresentOrElse(
                            attributes::add,
                            () -> log.info("Attribute not found: {}", option)
                    );
        }
        return attributes;
    }

    private ProgressBar buildProgressBar() {
        return new ProgressBarBuilder()
                .setTaskName("Attributes")
                .setStyle(ProgressBarStyle.ASCII)
                .setInitialMax(source.rootSize())
                .showSpeed()
                .setConsumer(new ConsoleProgressBarConsumer(System.out))
                .build();
    }
}

package me.supcheg.sanparser.santech.attribute.warmup;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.progress.ProgressBarFactory;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;
import java.util.concurrent.Executor;

import static com.pivovarit.collectors.ParallelCollectors.parallel;
import static java.util.stream.Collectors.counting;

@Component
@RequiredArgsConstructor
class ParallelSantechItemAttributeWarmup implements SantechItemAttributeWarmup {
    private final SantechItemSource source;

    private final Executor executor;
    private final int parallelism;

    private final ProgressBarFactory progressBarFactory;

    @Override
    public void warmup(Iterable<? extends SantechItemAttribute<?>> attributes) {
        try (var bar = progressBarFactory.createProgressBar(titleFor(attributes))) {
            source.items()
                    .peek(item -> attributes.forEach(item::attribute))
                    .collect(parallel(__ -> bar.step(), counting(), executor, parallelism))
                    .join();
        }
    }

    private static String titleFor(Iterable<? extends SantechItemAttribute<?>> attributes) {
        StringJoiner joiner = new StringJoiner(", ");
        for (var attribute : attributes) {
            String key = attribute.key();
            joiner.add(key.length() > 4 ? key.substring(0, 4) : key);
        }
        return joiner.toString();
    }
}

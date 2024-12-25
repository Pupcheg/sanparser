package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.SantechUriSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pivovarit.collectors.ParallelCollectors.parallel;

@RequiredArgsConstructor
public abstract class ProgressBarRunner implements ApplicationRunner {
    @Autowired
    private Executor executor;
    @Autowired
    private SantechUriSource uriSource;

    private final String name;
    private final Supplier<Stream<?>> streamSupplier;

    @Override
    public void run(ApplicationArguments args) {
        try (var bar = buildProgressBar()) {
            streamSupplier.get()
                    .collect(parallel(i -> bar.step(), Collectors.counting(), executor, 6))
                    .thenRun(() -> System.exit(0))
                    .join();
        }
    }

    private ProgressBar buildProgressBar() {
        return new ProgressBarBuilder()
                .setTaskName(name)
                .setInitialMax(uriSource.size())
                .showSpeed()
                .setConsumer(new ConsoleProgressBarConsumer(System.out))
                .build();
    }
}

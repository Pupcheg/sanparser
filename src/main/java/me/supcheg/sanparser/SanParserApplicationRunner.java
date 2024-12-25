package me.supcheg.sanparser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.association.AssociationsSource;
import me.supcheg.sanparser.uri.SantechUriSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.pivovarit.collectors.ParallelCollectors.parallel;

@Slf4j
@RequiredArgsConstructor
@Component
public class SanParserApplicationRunner implements ApplicationRunner {
    private final AssociationsSource associationsSource;
    private final SantechUriSource uriSource;
    private final Executor executor;

    @Override
    public void run(ApplicationArguments args) {
        try (var bar = buildProgressBar()) {
            associationsSource.associations()
                    .collect(parallel(i -> bar.step(), Collectors.counting(), executor, 6))
                    .thenRun(() -> System.exit(0))
                    .join();
        }
    }

    private ProgressBar buildProgressBar() {
        return new ProgressBarBuilder()
                .setTaskName("Associations")
                .setInitialMax(uriSource.size())
                .showSpeed()
                .setConsumer(new ConsoleProgressBarConsumer(System.out))
                .build();
    }
}

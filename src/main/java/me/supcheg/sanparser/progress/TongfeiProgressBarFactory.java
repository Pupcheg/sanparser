package me.supcheg.sanparser.progress;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TongfeiProgressBarFactory implements ProgressBarFactory {
    private final SantechItemSource source;

    @Override
    public ProgressBar createProgressBar(String title) {
        return new ProgressBar() {
            private final me.tongfei.progressbar.ProgressBar upstream =
                    new ProgressBarBuilder()
                            .setTaskName(title)
                            .setInitialMax(source.rootSize())
                            .setStyle(ProgressBarStyle.ASCII)
                            .showSpeed()
                            .setConsumer(new ConsoleProgressBarConsumer(System.out))
                            .build();

            @Override
            public void step() {
                upstream.step();
            }

            @Override
            public void close() {
                upstream.close();
            }
        };
    }
}

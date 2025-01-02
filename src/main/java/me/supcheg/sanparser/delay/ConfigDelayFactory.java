package me.supcheg.sanparser.delay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ConfigDelayFactory implements DelayFactory {
    @Value("${delay.root}")
    private Duration root;
    @Value("${delay.delta}")
    private Duration delta;

    @Override
    public Duration randomDelay() {
        return root.plus(randomDelta());
    }

    private Duration randomDelta() {
        long deltaMillis = delta.toMillis();
        long millis = ThreadLocalRandom.current().nextLong(-deltaMillis, deltaMillis);
        return Duration.ofMillis(millis);
    }
}

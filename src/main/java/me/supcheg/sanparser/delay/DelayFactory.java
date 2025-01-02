package me.supcheg.sanparser.delay;

import java.time.Duration;

public interface DelayFactory {
    Duration randomDelay();
}

package me.supcheg.sanparser.progress;

import java.io.Closeable;
import java.util.Optional;

public interface ProgressBar extends Closeable {
    void step();

    default Object step(Object ignored) {
        step();
        return Optional.empty();
    }

    @Override
    void close();
}

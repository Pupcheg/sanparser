package me.supcheg.sanparser.download.recognizer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.delay.DelayFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
public class DelayedUriRecognizer implements UriRecognizer {
    private final UriRecognizer delegate;
    private final DelayFactory delayFactory;

    @Override
    public boolean canRecognize(URI uri) {
        return delegate.canRecognize(uri);
    }

    @Override
    public Optional<InputStream> recognize(URI uri) {
        waitDelay();
        return delegate.recognize(uri);
    }

    @SneakyThrows
    private void waitDelay() {
        Thread.sleep(delayFactory.randomDelay());
    }
}

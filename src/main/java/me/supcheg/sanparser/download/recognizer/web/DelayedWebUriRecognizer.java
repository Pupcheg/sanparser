package me.supcheg.sanparser.download.recognizer.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.delay.DelayFactory;
import me.supcheg.sanparser.download.recognizer.Recognizer;
import me.supcheg.sanparser.download.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@Recognizer
@RequiredArgsConstructor
@Component
class DelayedWebUriRecognizer implements UriRecognizer {
    private final WebUriRecognizer delegate;
    private final DelayFactory delayFactory;

    @Override
    public boolean canRecognize(URI uri) {
        return delegate.canRecognize(uri);
    }

    @Override
    public Optional<InputStream> recognize(URI uri) throws IOException {
        waitDelay();
        return delegate.recognize(uri);
    }

    @SneakyThrows(InterruptedException.class)
    private void waitDelay() {
        Thread.sleep(delayFactory.randomDelay());
    }
}

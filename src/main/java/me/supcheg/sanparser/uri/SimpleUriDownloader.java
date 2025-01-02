package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.uri.recognizer.Recognizer;
import me.supcheg.sanparser.uri.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SimpleUriDownloader implements UriDownloader {
    @Recognizer
    private final List<UriRecognizer> recognizers;

    @SneakyThrows
    @Override
    public Optional<InputStream> download(URI uri) {
        return recognizeInputStream(uri);
    }

    private Optional<InputStream> recognizeInputStream(URI uri) throws IOException {
        for (UriRecognizer recognizer : recognizers) {
            if (recognizer.canRecognize(uri)) {
                return recognizer.recognize(uri);
            }
        }

        throw new IllegalArgumentException("Unsupported uri: " + uri);
    }
}

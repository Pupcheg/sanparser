package me.supcheg.sanparser.download;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.download.recognizer.Recognizer;
import me.supcheg.sanparser.download.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class RecognizingUriDownloader implements UriDownloader {
    @Recognizer
    private final List<UriRecognizer> recognizers;

    @SneakyThrows
    @Override
    public Optional<InputStream> download(URI uri) {
        for (UriRecognizer recognizer : recognizers) {
            if (recognizer.canRecognize(uri)) {
                return recognizer.recognize(uri);
            }
        }

        throw new IllegalArgumentException("Unsupported uri: " + uri);
    }
}

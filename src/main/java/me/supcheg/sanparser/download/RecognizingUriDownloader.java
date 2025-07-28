package me.supcheg.sanparser.download;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.download.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class RecognizingUriDownloader implements UriDownloader {
    private final List<UriRecognizer> recognizers;

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

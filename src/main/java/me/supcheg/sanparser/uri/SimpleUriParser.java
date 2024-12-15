package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.recognizer.UriRecognizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SimpleUriParser implements UriParser {
    private final List<UriRecognizer> recognizers;

    @Override
    public Mono<Document> parse(URI uri) {
        return DataBufferUtils.join(recognizeDataBufferFlux(uri))
                .handle((dataBuffer, sink) -> {
                    try (var in = dataBuffer.asInputStream(true)) {
                        sink.next(Jsoup.parse(in, StandardCharsets.UTF_8.name(), ""));
                    } catch (IOException e) {
                        sink.error(e);
                    }
                });
    }

    private Flux<DataBuffer> recognizeDataBufferFlux(URI uri) {
        for (UriRecognizer recognizer : recognizers) {
            try {
                return recognizer.recognize(uri);
            } catch (UriRecognizer.UnsupportedUriException ignored) {
            }
        }

        throw new IllegalArgumentException("Unsupported uri: " + uri);
    }
}

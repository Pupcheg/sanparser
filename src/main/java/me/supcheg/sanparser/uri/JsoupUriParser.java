package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JsoupUriParser implements UriParser {
    private final UriDownloader uriDownloader;

    @Override
    public Optional<Document> parse(URI uri) {
        return uriDownloader.download(uri)
                .map(this::parseInputStream);
    }

    @SneakyThrows
    private Document parseInputStream(InputStream in) {
        return Jsoup.parse(in, StandardCharsets.UTF_8.name(), "");
    }
}

package me.supcheg.sanparser.document;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
class DefaultDocumentParser implements DocumentParser {
    private static final String UTF_8_NAME = StandardCharsets.UTF_8.name();

    @SneakyThrows
    @Override
    public Document parse(InputStream in) {
        return Jsoup.parse(in, UTF_8_NAME, "");
    }

    @Override
    public Document parse(String string) {
        return Jsoup.parse(string);
    }
}

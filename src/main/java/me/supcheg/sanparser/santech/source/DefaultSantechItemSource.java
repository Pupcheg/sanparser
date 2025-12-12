package me.supcheg.sanparser.santech.source;

import me.supcheg.sanparser.document.DocumentParser;
import me.supcheg.sanparser.download.UriDownloader;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.repository.SantechItemRepository;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

record DefaultSantechItemSource(
        UriDownloader uriDownloader,
        DocumentParser documentParser,
        SantechItemRepository itemRepository,
        URI catalog
) implements SantechItemSource {
    @Override
    public Stream<SantechItem> items() {
        return Stream.of(catalog)
                .map(uriDownloader::download)
                .<InputStream>mapMulti(Optional::ifPresent)
                .map(documentParser::parse)
                .flatMap(Document::stream)
                .filter(element -> element.tagName().equalsIgnoreCase("url"))
                .map(element -> URI.create(element.ownText().strip()))
                .map(itemRepository::item);
    }
}

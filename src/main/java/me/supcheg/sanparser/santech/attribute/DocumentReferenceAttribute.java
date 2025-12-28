package me.supcheg.sanparser.santech.attribute;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.document.DocumentParser;
import me.supcheg.sanparser.document.DocumentReference;
import me.supcheg.sanparser.download.UriDownloader;
import me.supcheg.sanparser.santech.SantechItem;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
class DocumentReferenceAttribute implements SantechItemAttribute<DocumentReference> {
    private final UriDownloader uriDownloader;
    private final DocumentParser documentParser;

    @Override
    public String key() {
        return "document_reference";
    }

    @Override
    public Optional<DocumentReference> find(SantechItem item) {
        return parseDocument(item)
                .map(document ->
                        new DocumentReferenceImpl(
                                () -> parseDocument(item).orElseThrow(),
                                document
                        )
                );
    }

    private Optional<Document> parseDocument(SantechItem item) {
        return Optional.of(item.uri())
                .flatMap(uriDownloader::download)
                .map(documentParser::parse);
    }

    @AllArgsConstructor
    static class DocumentReferenceImpl implements DocumentReference {
        private final Supplier<Document> supplier;
        private volatile Reference<@Nullable Document> cache;

        public DocumentReferenceImpl(Supplier<Document> supplier, Document document) {
            this.supplier = supplier;
            this.cache = new WeakReference<>(document);
        }

        @Override
        public Document document() {
            if (cache.refersTo(null)) {
                synchronized (this) {
                    if (cache.refersTo(null)) {
                        Document document = supplier.get();
                        cache = new WeakReference<>(document);
                        return document;
                    }
                }
            }
            return Objects.requireNonNull(cache.get());
        }
    }
}

package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.document.DocumentReference;
import me.supcheg.sanparser.santech.SantechItem;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class DocumentAttribute implements SantechItemAttribute<Document> {
    private final SantechItemAttribute<DocumentReference> documentReference;

    @Override
    public String key() {
        return "document";
    }

    @Override
    public Optional<Document> find(SantechItem item) {
        return item.attribute(documentReference)
                .map(DocumentReference::document);
    }
}

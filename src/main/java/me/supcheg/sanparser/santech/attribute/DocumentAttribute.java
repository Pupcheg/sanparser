package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.uri.parse.UriParser;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class DocumentAttribute implements SantechItemAttribute<Document> {
    private final SantechItemAttribute<URI> uri;
    private final UriParser uriParser;

    @Override
    public String key() {
        return "document";
    }

    @Override
    public Optional<Document> find(SantechItem item) {
       return item.attribute(uri)
                .flatMap(uriParser::parse);
    }
}

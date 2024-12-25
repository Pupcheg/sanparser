package me.supcheg.sanparser.properties;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import me.supcheg.sanparser.uri.UriParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ParsingItemWithPropertiesResolver implements ItemWithPropertiesResolver {
    private final SantechIdentifierResolver identifierResolver;
    private final UriParser uriParser;

    @Override
    public Optional<ItemWithProperties> resolveItemWithProperties(URI uri) {
        return identifierResolver.resolveSantechIdentifier(uri)
                .flatMap(identifier ->
                        resolveProperties(uri)
                                .map(properties -> new ItemWithProperties(identifier, properties))
                );
    }

    private Optional<Map<String, String>> resolveProperties(URI uri) {
        return uriParser.parse(uri)
                .map(this::resolveProperties);
    }

    private Map<String, String> resolveProperties(Document doc) {
        Map<String, String> properties = new HashMap<>();
        doc.getAllElements().stream()
                .filter(element -> element.className().strip().equals("ss-product-property"))
                .map(Element::children)
                .flatMap(Collection::stream)
                .filter(element -> !element.ownText().isEmpty())
                .filter(element -> element.nextElementSibling() != null)
                .forEach(element -> {
                    String key = element.ownText().strip();

                    Element sibling = element.nextElementSibling();
                    Objects.requireNonNull(sibling);

                    Element valueElement = sibling.firstElementChild();
                    Objects.requireNonNull(valueElement);

                    String value = valueElement.text().strip();
                    properties.put(key, value);
                });
        return properties;
    }
}

package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cache.CacheableSantechItemAttributeImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Component
class PropertiesAttribute extends CacheableSantechItemAttributeImpl<Map<String, String>> {
    private final SantechItemAttribute<Document> document;

    @Override
    public String key() {
        return "properties";
    }

    @Override
    public Optional<Map<String, String>> findIternal(SantechItem attributed) {
        return attributed.attribute(document)
                .map(PropertiesAttribute::resolveProperties);
    }

    private static Map<String, String> resolveProperties(Document doc) {
       return doc.stream()
                .filter(element -> element.className().strip().equals("ss-product-property"))
                .map(Element::children)
                .flatMap(Collection::stream)
                .filter(element -> !element.ownText().isEmpty())
                .filter(element -> element.nextElementSibling() != null)
                .map(element -> {
                    String key = element.ownText().strip();

                    Element sibling = element.nextElementSibling();
                    Objects.requireNonNull(sibling);

                    Element valueElement = sibling.firstElementChild();
                    Objects.requireNonNull(valueElement);

                    String value = valueElement.text().strip();
                    return Pair.of(key, value);
                })
                .collect(toMap(Pair::getFirst, Pair::getSecond, (left, right) -> right));
    }
}

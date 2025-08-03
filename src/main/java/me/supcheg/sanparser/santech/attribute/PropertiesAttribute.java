package me.supcheg.sanparser.santech.attribute;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttributeImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableMap;

@RequiredArgsConstructor
@Component
class PropertiesAttribute extends CacheableSantechItemAttributeImpl<Map<String, String>> {
    private final SantechItemAttribute<Document> document;

    @Override
    public String key() {
        return "properties";
    }

    @Override
    public TypeReference<Map<String, String>> type() {
        return new TypeReference<>() {};
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
                .collect(toUnmodifiableMap(Pair::getFirst, Pair::getSecond, (left, right) -> right));
    }
}

package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cache.CacheableSantechItemAttributeImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class CategoryAttribute extends CacheableSantechItemAttributeImpl<String> {
    private final SantechItemAttribute<Document> document;

    @Override
    public String key() {
        return "category";
    }

    @Override
    public Optional<String> findIternal(SantechItem attributed) {
        return attributed.attribute(document)
                .flatMap(CategoryAttribute::resolveCategory);
    }

    private static Optional<String> resolveCategory(Document doc) {
        return doc.stream()
                .filter(element -> element.className().strip().equals("ss-breadcrumbs"))
                .map(element -> {
                    Element categoryListElement = element.firstElementChild();
                    Objects.requireNonNull(categoryListElement);

                    Element preLastCategoryElement = categoryListElement.lastElementChild();
                    Objects.requireNonNull(preLastCategoryElement);
                    return preLastCategoryElement.text().strip();
                })
                .findFirst();
    }
}

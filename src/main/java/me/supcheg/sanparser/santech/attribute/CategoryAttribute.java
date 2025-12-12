package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechCategory;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttributeImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class CategoryAttribute extends CacheableSantechItemAttributeImpl<SantechCategory> {
    private final SantechItemAttribute<Document> document;

    @Override
    public String key() {
        return "category";
    }

    @Override
    public TypeReference<SantechCategory> type() {
        return new TypeReference<>() {
        };
    }

    @Override
    public Optional<SantechCategory> findIternal(SantechItem attributed) {
        return attributed.attribute(document)
                .flatMap(CategoryAttribute::resolveCategory);
    }

    private static Optional<SantechCategory> resolveCategory(Document doc) {
        return doc.stream()
                .filter(element -> element.className().strip().equals("ss-breadcrumbs"))
                .map(element -> {
                    Element categoryListElement = element.firstElementChild();
                    Objects.requireNonNull(categoryListElement);

                    Element preLastCategoryElement = categoryListElement.lastElementChild();
                    Objects.requireNonNull(preLastCategoryElement);
                    return preLastCategoryElement.text().strip();
                })
                .map(SantechCategory::new)
                .findFirst();
    }
}

package me.supcheg.sanparser.santech.attribute;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cache.CacheableSantechItemAttributeImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Slf4j
@RequiredArgsConstructor
@Component
class SantechIdentifierAttribute extends CacheableSantechItemAttributeImpl<SantechIdentifier> {
    private final SantechItemAttribute<Document> document;
    private final ObjectMapper objectMapper;

    @Override
    public String key() {
        return "santech_identifier";
    }

    @Override
    public Optional<SantechIdentifier> findIternal(SantechItem attributed) {
        return attributed.attribute(document)
                .flatMap(this::resolveSantechIdentifier);
    }

    public Optional<SantechIdentifier> resolveSantechIdentifier(Document document) {
        return selectNumber(document)
                .flatMap(number ->
                        selectFullId(document)
                                .map(defaultPostData ->
                                        new SantechIdentifier(
                                                number,
                                                defaultPostData.variant_id(),
                                                defaultPostData.item_id()
                                        )
                                )
                );
    }

    private Optional<String> selectNumber(Document document) {
        return document.stream()
                .filter(element -> element.ownText().strip().equals("Номенклатурный номер"))
                .map(element -> {
                    Element sibling = element.nextElementSibling();
                    Objects.requireNonNull(sibling);

                    Element numberElement = sibling.firstElementChild();
                    Objects.requireNonNull(numberElement);

                    return numberElement.ownText().strip();
                })
                .findFirst();
    }

    private Optional<DefaultPostData> selectFullId(Document document) {
        return document.stream()
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-catalog-products-slider"))
                .map(element -> element.attr("data-catalog-products-slider__options"))
                .filter(not(String::isEmpty))
                .map(this::readPostData)
                .findFirst();
    }

    @SneakyThrows
    private DefaultPostData readPostData(String raw) {
        return objectMapper.treeToValue(
                objectMapper.readTree(raw)
                        .path("defaultPostData"),
                DefaultPostData.class
        );
    }

    record DefaultPostData(
            String variant_id,
            String item_id
    ) {
    }
}

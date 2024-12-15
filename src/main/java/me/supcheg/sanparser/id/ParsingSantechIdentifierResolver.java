package me.supcheg.sanparser.id;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.uri.UriParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
public class ParsingSantechIdentifierResolver implements SantechIdentifierResolver {
    private final UriParser uriParser;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<SantechIdentifier> resolveSantechIdentifier(URI uri) {
        return Mono.just(uri)
                .flatMap(uriParser::parse)
                .flatMap(document ->
                        Mono.zip(
                                selectNumber(document),
                                selectFullId(document),
                                (number, defaultPostData) -> new SantechIdentifier(
                                        number,
                                        defaultPostData.variant_id(),
                                        defaultPostData.item_id()
                                )
                        )
                );
    }

    private Mono<String> selectNumber(Document document) {
        return Flux.fromIterable(document.getAllElements())
                .filter(element -> element.ownText().strip().equals("Номенклатурный номер"))
                .map(element -> {
                    Element sibling = element.nextElementSibling();
                    Objects.requireNonNull(sibling);

                    Element numberElement = sibling.firstElementChild();
                    Objects.requireNonNull(numberElement);

                    return numberElement.ownText().strip();
                })
                .next();
    }

    private Mono<DefaultPostData> selectFullId(Document document) {
        return Flux.fromIterable(document.getAllElements())
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-catalog-products-slider"))
                .map(element -> element.attr("data-catalog-products-slider__options"))
                .filter(not(String::isEmpty))
                .map(this::readPostData)
                .next();
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

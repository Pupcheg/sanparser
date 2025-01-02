package me.supcheg.sanparser.category;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import me.supcheg.sanparser.uri.UriParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ParsingItemWithCategoryResolver implements ItemWithCategoryResolver {
    private final UriParser uriParser;
    private final SantechIdentifierResolver identifierResolver;

    @Override
    public Optional<ItemWithCategory> resolveItemWithCategory(URI uri) {
        return identifierResolver.resolveSantechIdentifier(uri)
                .flatMap(identifier ->
                        uriParser.parse(uri)
                                .flatMap(this::resolveCategory)
                                .map(group -> new ItemWithCategory(identifier, group))
                );
    }

    private Optional<String> resolveCategory(Document doc) {
        return doc.stream()
                .filter(element -> element.className().strip().equals("ss-breadcrumbs"))
                .findFirst()
                .map(element -> {
                    Element categoryListElement = element.firstElementChild();
                    Objects.requireNonNull(categoryListElement);

                    Element preLastCategoryElement = categoryListElement.lastElementChild();
                    Objects.requireNonNull(preLastCategoryElement);
                    return preLastCategoryElement.text().strip();
                });
    }
}

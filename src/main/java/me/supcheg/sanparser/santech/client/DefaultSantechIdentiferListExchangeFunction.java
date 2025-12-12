package me.supcheg.sanparser.santech.client;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.document.DocumentParser;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.repository.SantechItemRepository;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
class DefaultSantechIdentiferListExchangeFunction implements SantechIdentiferListExchangeFunction {
    private final SantechItemAttribute<SantechIdentifier> identifier;
    private final ObjectMapper objectMapper;
    private final SantechItemRepository itemRepository;
    private final DocumentParser documentParser;

    @Override
    public List<SantechIdentifier> exchange(HttpRequest request,
                                            ConvertibleClientHttpResponse response) {
        String content = objectMapper.readTree(response.bodyTo(String.class))
                .path("content")
                .stringValue();

        return documentParser.parse(content)
                .stream()
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-slider__item"))
                .map(element -> element.child(0).attr("href"))
                .filter(not(String::isEmpty))
                .map(href -> URI.create("https://www.santech.ru" + href))
                .map(itemRepository::item)
                .map(item -> item.attribute(identifier))
                .<SantechIdentifier>mapMulti(Optional::ifPresent)
                .toList();
    }
}

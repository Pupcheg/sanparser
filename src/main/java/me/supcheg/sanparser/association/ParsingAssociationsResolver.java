package me.supcheg.sanparser.association;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.id.SantechIdentifier;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
public class ParsingAssociationsResolver implements AssociationsResolver {
    private final RestClient client;
    private final SantechIdentifierResolver santechIdentifierResolver;
    private final ObjectMapper objectMapper;
    @Value("${santech.associations.analogues-uri}")
    private URI analogues;
    @Value("${santech.associations.associations-uri}")
    private URI associations;

    @Override
    public List<SantechIdentifier> analogues(SantechIdentifier root) {
        String response = client.post()
                .uri(analogues)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(MultiValueMap.fromSingleValue(
                        Map.of(
                                "ajax_load", "true",
                                "variant_id", root.variantId()
                        )
                ))
                .retrieve()
                .body(String.class);
        return resolveSantechIdentifiers(response);
    }

    @Override
    public List<SantechIdentifier> associations(SantechIdentifier root) {
        String response = client.post()
                .uri(associations)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(MultiValueMap.fromSingleValue(
                        Map.of(
                                "ajax_load", "true",
                                "variant_id", root.variantId(),
                                "item_id", root.itemId()
                        )
                ))
                .retrieve()
                .body(String.class);
        return resolveSantechIdentifiers(response);
    }

    @SneakyThrows
    private List<SantechIdentifier> resolveSantechIdentifiers(String raw) {
       String content = objectMapper.readTree(raw)
                .path("content")
                .textValue();

        return Jsoup.parse(content).getAllElements().stream()
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-slider__item"))
                .map(element -> element.child(0).attr("href"))
                .filter(not(String::isEmpty))
                .map(href -> URI.create("https://www.santech.ru" + href))
                .map(santechIdentifierResolver::resolveSantechIdentifier)
                .<SantechIdentifier>mapMulti(Optional::ifPresent)
                .toList();
    }

}

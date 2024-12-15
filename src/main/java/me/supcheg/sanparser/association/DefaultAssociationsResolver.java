package me.supcheg.sanparser.association;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.id.SantechIdentifier;
import me.supcheg.sanparser.id.SantechIdentifierResolver;
import me.supcheg.sanparser.id.SantechIdentifierSource;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

import static java.util.function.Predicate.not;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@RequiredArgsConstructor
@Component
public class DefaultAssociationsResolver implements AssociationsResolver {
    private final SantechIdentifierSource identifierSource;
    private final WebClient webClient;
    private final SantechIdentifierResolver santechIdentifierResolver;
    private final ObjectMapper objectMapper;
    @Value("${santech.associations.analogues-uri}")
    private URI analogues;
    @Value("${santech.associations.variants-uri}")
    private URI variants;

    @Override
    public Flux<LazyAssociatedItem> associations() {
        return identifierSource.santechIdentifiers()
                .map(identifier ->
                        new LazyAssociatedItem(
                                identifier,
                                analogues(identifier),
                                variants(identifier)
                        )
                );
    }

    private Flux<SantechIdentifier> analogues(SantechIdentifier root) {
        return webClient.post()
                .uri(analogues)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        fromFormData("ajax_load", "true")
                                .with("variant_id", root.variantId())
                )
                .exchangeToMono(clientResponse ->
                        clientResponse.body(
                                (clientHttpResponse, ctx) ->
                                        DataBufferUtils.join(clientHttpResponse.getBody())
                        )
                )
                .map(this::readDataBuffer)
                .flatMapMany(response -> Flux.fromIterable(Jsoup.parse(response).getAllElements()))
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-slider__item"))
                .map(element -> element.child(0).attr("href"))
                .filter(not(String::isEmpty))
                .map(href -> URI.create("https://www.santech.ru" + href))
                .flatMap(santechIdentifierResolver::resolveSantechIdentifier);
    }

    @SneakyThrows
    private String readDataBuffer(DataBuffer buffer) {
        try (var in = buffer.asInputStream(true)) {
            return objectMapper.readTree(in)
                    .path("content")
                    .textValue();
        }
    }

    private Flux<SantechIdentifier> variants(SantechIdentifier root) {
        return webClient.post()
                .uri(variants)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        fromFormData("ajax_load", "true")
                                .with("variant_id", root.variantId())
                                .with("item_id", root.itemId())
                )
                .exchangeToMono(clientResponse ->
                        clientResponse.body(
                                (clientHttpResponse, ctx) ->
                                        DataBufferUtils.join(clientHttpResponse.getBody())
                        )
                )
                .map(this::readDataBuffer)
                .flatMapMany(response -> Flux.fromIterable(Jsoup.parse(response).getAllElements()))
                .filter(element -> element.className().strip().equalsIgnoreCase("ss-slider__item"))
                .map(element -> element.child(0).attr("href"))
                .filter(not(String::isEmpty))
                .map(href -> URI.create("https://www.santech.ru" + href))
                .flatMap(santechIdentifierResolver::resolveSantechIdentifier);
    }
}

package me.supcheg.sanparser.santech.attribute;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cache.CacheableSantechItemAttributeImpl;
import me.supcheg.sanparser.santech.client.SantechIdentiferListExchangeFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class AnaloguesAttribute extends CacheableSantechItemAttributeImpl<List<SantechIdentifier>> {
    private final SantechItemAttribute<SantechIdentifier> identifier;

    private final RestClient client;
    private final SantechIdentiferListExchangeFunction santechIdentiferListExchangeFunction;

    @Value("${santech.associations.analogues-uri}")
    private URI analogues;

    @Override
    public String key() {
        return "analogues";
    }

    @Override
    public Optional<List<SantechIdentifier>> findIternal(SantechItem attributed) {
        return attributed.attribute(identifier)
                .map(this::analogues);
    }

    public List<SantechIdentifier> analogues(SantechIdentifier root) {
        return client.post()
                .uri(analogues)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(MultiValueMap.fromSingleValue(
                        Map.of(
                                "ajax_load", "true",
                                "variant_id", root.variantId()
                        )
                ))
                .exchange(santechIdentiferListExchangeFunction);
    }

    @SneakyThrows
    @Override
    protected List<SantechIdentifier> convertFromString(String raw) {
        return objectMapper.readValue(raw, new TypeReference<>() {});
    }
}

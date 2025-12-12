package me.supcheg.sanparser.santech.attribute;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.cacheable.CacheableSantechItemAttributeImpl;
import me.supcheg.sanparser.santech.client.SantechIdentiferListExchangeFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class AssociationsAttribute extends CacheableSantechItemAttributeImpl<List<SantechIdentifier>> {
    private final SantechItemAttribute<SantechIdentifier> identifier;

    private final RestClient client;
    private final SantechIdentiferListExchangeFunction santechIdentiferListExchangeFunction;

    @Value("${santech.associations.associations-uri}")
    private URI associations;

    @Override
    public String key() {
        return "associations";
    }

    @Override
    public TypeReference<List<SantechIdentifier>> type() {
        return new TypeReference<>() {
        };
    }

    @Override
    public Optional<List<SantechIdentifier>> findIternal(SantechItem attributed) {
        return attributed.attribute(identifier)
                .map(this::associations);
    }

    public List<SantechIdentifier> associations(SantechIdentifier root) {
        return client.post()
                .uri(associations)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(MultiValueMap.fromSingleValue(
                        Map.of(
                                "ajax_load", "true",
                                "variant_id", root.variantId(),
                                "item_id", root.itemId()
                        )
                ))
                .exchange(santechIdentiferListExchangeFunction);
    }
}

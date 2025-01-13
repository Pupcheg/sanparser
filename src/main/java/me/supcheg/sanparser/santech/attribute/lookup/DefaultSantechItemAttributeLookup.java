package me.supcheg.sanparser.santech.attribute.lookup;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DefaultSantechItemAttributeLookup implements SantechItemAttributeLookup {
    private final List<SantechItemAttribute<?>> attributes;

    @Override
    public Optional<SantechItemAttribute<?>> find(String key) {
        return attributes.stream()
                .filter(attribute -> key.equalsIgnoreCase(attribute.key()))
                .findFirst();
    }

    @Override
    public List<SantechItemAttribute<?>> all() {
        return Collections.unmodifiableList(attributes);
    }
}

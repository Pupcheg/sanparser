package me.supcheg.sanparser.santech.attribute.lookup;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DefaultSantechItemAttributeLookup implements SantechItemAttributeLookup {
    private final List<SantechItemAttribute<?>> attributes;

    @Override
    public <T> Optional<SantechItemAttribute<T>> find(String key) {
        for (SantechItemAttribute<?> attribute : attributes) {
            if (attribute.key().equalsIgnoreCase(key)) {
                return Optional.of((SantechItemAttribute<T>) attribute);
            }
        }
        return Optional.empty();
    }
}

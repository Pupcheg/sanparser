package me.supcheg.sanparser.santech;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.net.URI;
import java.util.Optional;

public interface SantechItem {
    URI uri();

    default <T> Optional<T> attribute(SantechItemAttribute<T> attribute) {
        return attribute.find(this);
    }
}

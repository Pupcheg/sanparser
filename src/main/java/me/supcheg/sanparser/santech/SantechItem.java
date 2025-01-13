package me.supcheg.sanparser.santech;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.util.Optional;

public interface SantechItem {
    default <T> Optional<T> attribute(SantechItemAttribute<T> attribute) {
        return attribute.find(this);
    }
}

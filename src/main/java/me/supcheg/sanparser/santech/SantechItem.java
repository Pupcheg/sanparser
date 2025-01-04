package me.supcheg.sanparser.santech;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.util.Optional;

public interface SantechItem {
    <T> Optional<T> attribute(SantechItemAttribute<T> attribute);
}

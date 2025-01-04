package me.supcheg.sanparser.santech.attribute.lookup;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.util.Optional;

public interface SantechItemAttributeLookup {
    <T> Optional<SantechItemAttribute<T>> find(String key);
}

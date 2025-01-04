package me.supcheg.sanparser.santech.attribute;

import me.supcheg.sanparser.santech.SantechItem;

import java.util.Optional;

public interface SantechItemAttribute<T> {
    String key();

    Optional<T> find(SantechItem item);
}

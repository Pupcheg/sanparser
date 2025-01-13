package me.supcheg.sanparser.santech.attribute.lookup;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

import java.util.List;
import java.util.Optional;

public interface SantechItemAttributeLookup {
    Optional<SantechItemAttribute<?>> find(String key);

    List<SantechItemAttribute<?>> all();
}

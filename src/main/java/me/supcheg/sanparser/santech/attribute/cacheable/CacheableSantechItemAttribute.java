package me.supcheg.sanparser.santech.attribute.cacheable;

import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import tools.jackson.core.type.TypeReference;

public interface CacheableSantechItemAttribute<T> extends SantechItemAttribute<T> {
    TypeReference<T> type();
}

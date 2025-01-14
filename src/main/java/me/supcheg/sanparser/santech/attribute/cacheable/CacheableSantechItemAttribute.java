package me.supcheg.sanparser.santech.attribute.cacheable;

import com.fasterxml.jackson.core.type.TypeReference;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

public interface CacheableSantechItemAttribute<T> extends SantechItemAttribute<T> {
    TypeReference<T> type();
}

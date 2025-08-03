package me.supcheg.sanparser.santech.attribute.warmup;

import com.google.common.collect.Lists;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;

public interface SantechItemAttributeWarmup {
    void warmup(Iterable<? extends SantechItemAttribute<?>> attributes);

    default void warmup(SantechItemAttribute<?> first, SantechItemAttribute<?>... others) {
        warmup(Lists.asList(first, others));
    }
}

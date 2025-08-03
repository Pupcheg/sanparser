package me.supcheg.sanparser.santech.source;

import me.supcheg.sanparser.santech.SantechItem;

import java.util.stream.Stream;

public interface SantechItemSource {
    Stream<SantechItem> items();

    default long rootSize() {
        return items().count();
    }
}

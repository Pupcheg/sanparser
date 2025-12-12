package me.supcheg.sanparser.santech.source;

import me.supcheg.sanparser.santech.SantechItem;

import java.util.stream.Stream;

record LimitedSantechItemSource(
        SantechItemSource downstream,
        int limit
) implements SantechItemSource {
    @Override
    public Stream<SantechItem> items() {
        return downstream.items()
                .limit(limit);
    }
}

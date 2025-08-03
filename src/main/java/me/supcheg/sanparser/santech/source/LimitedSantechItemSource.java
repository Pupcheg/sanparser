package me.supcheg.sanparser.santech.source;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;

import java.util.stream.Stream;

@RequiredArgsConstructor
class LimitedSantechItemSource implements SantechItemSource {
    private final SantechItemSource downstream;
    private final int limit;

    @Override
    public Stream<SantechItem> items() {
        return downstream.items()
                .limit(limit);
    }
}

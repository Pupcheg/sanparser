package me.supcheg.sanparser.santech.source;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.repository.SantechItemRepository;
import me.supcheg.sanparser.santech.uri.SantechUriSource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
class DefaultSantechItemSource implements SantechItemSource {
    private final SantechUriSource uriSource;
    private final SantechItemRepository itemRepository;

    @Override
    public Stream<SantechItem> items() {
        return uriSource.uris()
                .map(itemRepository::item);
    }

    @Override
    public long rootSize() {
        return uriSource.size();
    }
}

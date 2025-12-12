package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.book.variant.VariantBookWriter.VariantBookEntry;
import me.supcheg.sanparser.book.variant.VariantBookWriterFactory;
import me.supcheg.sanparser.condition.ConditionalOnMode;
import me.supcheg.sanparser.progress.ProgressBarFactory;
import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.warmup.SantechItemAttributeWarmup;
import me.supcheg.sanparser.santech.local.LocalIdentifierLookup;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnMode("variants")
@Order(0)
class VariantsCsvRunner implements ApplicationRunner {
    private final SantechItemSource source;
    private final ProgressBarFactory progressBarFactory;

    private final SantechItemAttribute<SantechIdentifier> santechIdentifier;
    private final SantechItemAttribute<List<SantechIdentifier>> analoguesAttribute;
    private final SantechItemAttribute<List<SantechIdentifier>> associationsAttribute;

    private final SantechItemAttributeWarmup warmup;

    private final LocalIdentifierLookup localIdentifierLookup;
    private final VariantBookWriterFactory bookFactory;

    @Value("${variants.out-path}")
    private Path outPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        warmup.warmup(
                santechIdentifier,
                analoguesAttribute,
                associationsAttribute
        );

        try (
                var book = bookFactory.newWriter(findMaxVariantsCount());
                var bar = progressBarFactory.createProgressBar("Variants book")
        ) {
            source.items()
                    .map(this::constructRecord)
                    .<VariantBookEntry>mapMulti(Optional::ifPresent)
                    .peek(book::append)
                    .forEach(bar::step);
            book.save(outPath);
            log.info("Saved {} at {}", outPath.getFileName(), outPath.toAbsolutePath());
        }
    }

    private int findMaxVariantsCount() {
        return source.items()
                .mapToInt(this::allVariantsCount)
                .max()
                .orElse(0);
    }

    private int allVariantsCount(SantechItem item) {
        return Stream.of(analoguesAttribute, associationsAttribute)
                .map(item::attribute)
                .<List<?>>mapMulti(Optional::ifPresent)
                .mapToInt(List::size)
                .sum();
    }

    private Optional<VariantBookEntry> constructRecord(SantechItem item) {
        return item.attribute(this.santechIdentifier)
                .filter(localIdentifierLookup::hasLocalIdentifier)
                .map(santechIdentifier ->
                        new VariantBookEntry(
                                santechIdentifier,
                                Stream.of(analoguesAttribute, associationsAttribute)
                                        .map(item::attribute)
                                        .<List<SantechIdentifier>>mapMulti(Optional::ifPresent)
                                        .flatMap(Collection::stream)
                                        .map(localIdentifierLookup::findLocalIdentifier)
                                        .<LocalIdentifier>mapMulti(Optional::ifPresent)
                                        .distinct()
                                        .toList()
                        )
                );
    }
}

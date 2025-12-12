package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.book.attribute.AttributeBookWriter.AttributeBookEntry;
import me.supcheg.sanparser.book.attribute.AttributeBookWriter.CategoryWithProperties;
import me.supcheg.sanparser.book.attribute.AttributeBookWriterFactory;
import me.supcheg.sanparser.condition.ConditionalOnMode;
import me.supcheg.sanparser.progress.ProgressBarFactory;
import me.supcheg.sanparser.santech.SantechCategory;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.warmup.SantechItemAttributeWarmup;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnMode("attributes")
@Order(0)
class AttributesRunner implements ApplicationRunner {
    private final SantechItemSource source;
    private final ProgressBarFactory progressBarFactory;
    private final AttributeBookWriterFactory bookFactory;

    private final SantechItemAttribute<SantechIdentifier> santechIdentifierSantechItemAttribute;
    private final SantechItemAttribute<SantechCategory> categoryAttribute;
    private final SantechItemAttribute<Map<String, String>> propertiesAttribute;

    private final SantechItemAttributeWarmup warmup;

    @Value("${attributes.out-path}")
    private Path outPath;

    @SneakyThrows
    @Override
    public void run(ApplicationArguments args) {
        warmup.warmup(
                santechIdentifierSantechItemAttribute,
                categoryAttribute,
                propertiesAttribute
        );

        try (
                var book = bookFactory.newWriter(findAllCategories());
                var bar = progressBarFactory.createProgressBar("Attributes book")
        ) {
            source.items()
                    .map(this::makeEntryFromItem)
                    .<AttributeBookEntry>mapMulti(Optional::ifPresent)
                    .peek(book::append)
                    .forEach(bar::step);

            book.save(outPath);
            log.info("Saved {} at {}", outPath.getFileName(), outPath.toAbsolutePath());
        }
    }

    private List<CategoryWithProperties> findAllCategories() {
        return source.items()
                .<CategoryWithProperties>mapMulti((item, downstream) ->
                        item.attribute(categoryAttribute)
                                .ifPresent(category ->
                                        item.attribute(propertiesAttribute)
                                                .ifPresent(properties ->
                                                        downstream.accept(
                                                                new CategoryWithProperties(
                                                                        category,
                                                                        List.copyOf(properties.keySet())
                                                                )
                                                        )
                                                )
                                )
                )
                .collect(groupingBy(CategoryWithProperties::category))
                .entrySet()
                .stream()
                .map(entry ->
                        new CategoryWithProperties(
                                entry.getKey(),
                                entry.getValue().stream()
                                        .map(CategoryWithProperties::properties)
                                        .flatMap(Collection::stream)
                                        .toList()
                        )
                )
                .toList();
    }

    private Optional<AttributeBookEntry> makeEntryFromItem(SantechItem item) {
        return item.attribute(santechIdentifierSantechItemAttribute)
                .flatMap(identifier ->
                        item.attribute(categoryAttribute)
                                .flatMap(category ->
                                        item.attribute(propertiesAttribute)
                                                .map(properties ->
                                                        new AttributeBookEntry(category, identifier, properties)
                                                )
                                )


                );
    }
}

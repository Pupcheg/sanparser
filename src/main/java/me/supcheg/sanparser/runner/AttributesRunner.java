package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.book.AttributeBookWriter;
import me.supcheg.sanparser.book.AttributeBookWriterFactory;
import me.supcheg.sanparser.progress.ProgressBarFactory;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.attribute.warmup.SantechItemAttributeWarmup;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnExpression("#{springApplicationArguments.containsOption('mode') && springApplicationArguments.getOptionValues('mode').contains('attributes')}")
@Order(0)
class AttributesRunner implements ApplicationRunner {
    private final SantechItemSource source;
    private final ProgressBarFactory progressBarFactory;
    private final AttributeBookWriterFactory bookFactory;

    private final SantechItemAttribute<SantechIdentifier> santechIdentifierSantechItemAttribute;
    private final SantechItemAttribute<String> categoryAttribute;
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

        record CategoryWithProperties(
                String group,
                Collection<String> properties
        ) {
        }

        Map<String, Set<String>> availablePropertiesForGroup = source.items()
                .<CategoryWithProperties>mapMulti((item, downstream) ->
                        item.attribute(categoryAttribute)
                                .ifPresent(category ->
                                        item.attribute(propertiesAttribute)
                                                .ifPresent(properties ->
                                                        downstream.accept(
                                                                new CategoryWithProperties(category, properties.keySet())
                                                        )
                                                )
                                )
                )
                .collect(
                        groupingBy(
                                CategoryWithProperties::group,
                                flatMapping(item -> item.properties().stream(), toUnmodifiableSet())
                        )
                );

        try (
                var book = bookFactory.newWriter(availablePropertiesForGroup);
                var bar = progressBarFactory.createProgressBar("Attributes book")
        ) {
            source.items()
                    .peek(item -> addItemToWorkbook(item, book))
                    .forEach(bar::step);

            book.save(outPath);
            log.info("Saved {} at {}", outPath.getFileName(), outPath.toAbsolutePath());
        }
    }

    private void addItemToWorkbook(SantechItem item, AttributeBookWriter book) {
        item.attribute(santechIdentifierSantechItemAttribute)
                .ifPresent(identifier ->
                        item.attribute(categoryAttribute)
                                .ifPresent(category ->
                                        item.attribute(propertiesAttribute)
                                                .ifPresent(properties ->
                                                        book.put(category, identifier, properties)
                                                )
                                )
                );
    }
}

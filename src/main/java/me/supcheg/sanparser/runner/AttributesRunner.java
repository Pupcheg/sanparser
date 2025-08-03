package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.book.AttributeBookWriter;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ProgressBar;
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
import java.util.concurrent.Executor;

import static com.pivovarit.collectors.ParallelCollectors.parallel;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnExpression("#{springApplicationArguments.containsOption('mode') && springApplicationArguments.getOptionValues('mode').contains('attributes')}")
@Order(0)
class AttributesRunner implements ApplicationRunner {
    private final Executor executor;
    private final int parallelism;

    private final SantechItemSource source;

    private final SantechItemAttribute<SantechIdentifier> santechIdentifierSantechItemAttribute;
    private final SantechItemAttribute<String> categoryAttribute;
    private final SantechItemAttribute<Map<String, String>> propertiesAttribute;

    private final AttributeBookWriter book;
    private final ProgressBar bar;

    @Value("${attributes.out-path}")
    private Path outPath;

    @SneakyThrows
    @Override
    public void run(ApplicationArguments args) {
        try (book; bar) {

            // warm up attributes
            source.items()
                    .peek(item -> {
                        item.attribute(santechIdentifierSantechItemAttribute);
                        item.attribute(categoryAttribute);
                        item.attribute(propertiesAttribute);
                    })
                    .collect(parallel(__ -> bar.step(), counting(), executor, parallelism))
                    .join();

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
            book.setAvailablePropertiesForGroup(availablePropertiesForGroup);

            source.items()
                    .forEach(this::addItemToWorkbook);

            book.save(outPath);
            log.info("Saved {} at {}", outPath.getFileName(), outPath.toAbsolutePath());
        }
    }

    private void addItemToWorkbook(SantechItem item) {
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

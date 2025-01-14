package me.supcheg.sanparser.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;
import me.supcheg.sanparser.santech.SantechItem;
import me.supcheg.sanparser.santech.attribute.SantechItemAttribute;
import me.supcheg.sanparser.santech.local.LocalIdentifierLookup;
import me.supcheg.sanparser.santech.source.SantechItemSource;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
        name = "mode",
        havingValue = "variants"
)
class VariantsCsvRunner implements ApplicationRunner {
    private final SantechItemSource source;
    private final SantechItemAttribute<SantechIdentifier> santechIdentifier;
    private final SantechItemAttribute<List<SantechIdentifier>> analoguesAttribute;
    private final SantechItemAttribute<List<SantechIdentifier>> associationsAttribute;
    private final LocalIdentifierLookup localIdentifierLookup;

    @Value("${variants.out_path}")
    private Path outPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] headers = Stream.concat(
                Stream.of("local_identifier"),
                IntStream.rangeClosed(1, findMaxVariantsCount())
                        .mapToObj(index -> "association-" + index)
        ).toArray(String[]::new);

        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .setAutoFlush(true)
                .build();

        try (
                var out = Files.newBufferedWriter(outPath);
                var printer = new CSVPrinter(out, csvFormat);
                var bar = buildProgressBar()
        ) {
            source.items()
                    .map(this::constructRecord)
                    .<List<String>>mapMulti(Optional::ifPresent)
                    .forEach(record -> {
                        try {
                            printer.printRecord(record);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        bar.step();
                    });
            log.info("Done");
        }
        log.info("Saved {} at {}", outPath.getFileName(), outPath.toAbsolutePath());
    }

    private int findMaxVariantsCount() {
        return source.items()
                .mapToInt(this::allVariantsCount)
                .max()
                .orElse(0);
    }

    private int allVariantsCount(SantechItem item) {
        return item.attribute(analoguesAttribute).map(List::size).orElse(0)
               + item.attribute(associationsAttribute).map(List::size).orElse(0);
    }

    private Optional<List<String>> constructRecord(SantechItem item) {
        return item.attribute(this.santechIdentifier)
                .filter(santechIdentifier ->
                        localIdentifierLookup.findLocalIdentifier(santechIdentifier).isPresent()
                )
                .map(localIdentifier ->
                        Stream.concat(
                                        Stream.of(localIdentifier.nomenclatureNumber()),
                                        Stream.concat(
                                                toLocalIdentifierStream(item.attribute(analoguesAttribute))
                                                        .map(LocalIdentifier::value),
                                                toLocalIdentifierStream(item.attribute(associationsAttribute))
                                                        .map(LocalIdentifier::value)
                                        )
                                )
                                .distinct()
                                .toList()
                );
    }

    private Stream<LocalIdentifier> toLocalIdentifierStream(Optional<List<SantechIdentifier>> variants) {
        return variants.map(List::stream).stream()
                .flatMap(stream -> stream
                        .map(localIdentifierLookup::findLocalIdentifier)
                        .mapMulti(Optional::ifPresent)
                );
    }

    private ProgressBar buildProgressBar() {
        return new ProgressBarBuilder()
                .setTaskName("variants csv file")
                .setStyle(ProgressBarStyle.ASCII)
                .setInitialMax(source.rootSize())
                .showSpeed()
                .setConsumer(new ConsoleProgressBarConsumer(System.out))
                .build();
    }
}

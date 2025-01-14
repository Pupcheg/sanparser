package me.supcheg.sanparser.santech.local;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
@Component
class TextFileLocalIdentifierLookup implements LocalIdentifierLookup {
    private final Map<String, LocalIdentifier> nomenclatureNumber2localIdentifier = new HashMap<>();

    @Value("${local_identifier.path}")
    private Path localIdentifierPath;

    @Autowired
    void loadTextFile(@Value("${local_identifier.split_pattern}") Pattern splitPattern) throws IOException {
        if (!Files.exists(localIdentifierPath)) {
            log.warn("Local identifiers file not exists: {}", localIdentifierPath);
            return;
        }

        if (!Files.isRegularFile(localIdentifierPath)) {
            log.error("Local identifiers file is not a file: {}", localIdentifierPath);
            return;
        }

        Map<String, LocalIdentifier> nomenclatureNumber2localIdentifier;
        try (Stream<String> lines = Files.lines(localIdentifierPath)) {
            nomenclatureNumber2localIdentifier = lines
                    .filter(not(String::isBlank))
                    .map(splitPattern::split)
                    .collect(
                            toMap(
                                    split -> split[0], // nomenclatureNumber
                                    split -> new LocalIdentifier(split[1])
                            )
                    );
        }
        if (!nomenclatureNumber2localIdentifier.isEmpty()) {
            log.info(
                    "Found {} identifiers (first: {}, last: {})",
                    nomenclatureNumber2localIdentifier.size(),
                    getFirst(nomenclatureNumber2localIdentifier.entrySet(), "<null>"),
                    getLast(nomenclatureNumber2localIdentifier.entrySet(), "<null>")
            );
            this.nomenclatureNumber2localIdentifier.putAll(nomenclatureNumber2localIdentifier);
        }
    }

    @Override
    public Optional<LocalIdentifier> findLocalIdentifier(SantechIdentifier santechIdentifier) {
        return Optional.ofNullable(nomenclatureNumber2localIdentifier.get(santechIdentifier.nomenclatureNumber()));
    }
}

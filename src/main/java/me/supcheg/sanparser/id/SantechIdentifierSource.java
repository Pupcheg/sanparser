package me.supcheg.sanparser.id;

import java.util.stream.Stream;

public interface SantechIdentifierSource {
    Stream<SantechIdentifier> santechIdentifiers();
}

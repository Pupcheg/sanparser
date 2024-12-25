package me.supcheg.sanparser.id;

import java.net.URI;
import java.util.Optional;

public interface SantechIdentifierResolver {
    Optional<SantechIdentifier> resolveSantechIdentifier(URI uri);
}

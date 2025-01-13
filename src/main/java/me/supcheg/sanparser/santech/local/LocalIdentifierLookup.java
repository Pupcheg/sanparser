package me.supcheg.sanparser.santech.local;

import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;

import java.util.Optional;

public interface LocalIdentifierLookup {
    Optional<LocalIdentifier> findLocalIdentifier(SantechIdentifier santechIdentifier);
}

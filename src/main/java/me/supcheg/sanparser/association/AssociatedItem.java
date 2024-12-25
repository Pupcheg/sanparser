package me.supcheg.sanparser.association;

import me.supcheg.sanparser.id.SantechIdentifier;

import java.util.List;

public record AssociatedItem(
        SantechIdentifier identifier,
        List<SantechIdentifier> analogues,
        List<SantechIdentifier> associations
) {
}

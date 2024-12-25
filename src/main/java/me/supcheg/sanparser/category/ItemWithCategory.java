package me.supcheg.sanparser.category;

import me.supcheg.sanparser.id.SantechIdentifier;

public record ItemWithCategory(
        SantechIdentifier id,
        String category
) {
}

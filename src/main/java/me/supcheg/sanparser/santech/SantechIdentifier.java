package me.supcheg.sanparser.santech;

import java.io.Serializable;

public record SantechIdentifier(
        String nomenclatureNumber,
        String variantId,
        String itemId
) implements Serializable {
}

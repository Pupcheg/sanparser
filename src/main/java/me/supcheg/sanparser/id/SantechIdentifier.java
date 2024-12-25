package me.supcheg.sanparser.id;

import java.io.Serializable;

public record SantechIdentifier(
        String nomenclatureNumber,
        String variantId,
        String itemId
) implements Serializable {

    public static SantechIdentifier fromString(String raw) {
        String[] split = raw.split(":", 3);
        return new SantechIdentifier(split[0], split[1], split[2]);
    }

    @Override
    public String toString() {
        return nomenclatureNumber + ':' + variantId + ':' + itemId;
    }
}

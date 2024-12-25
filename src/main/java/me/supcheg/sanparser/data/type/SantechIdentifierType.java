package me.supcheg.sanparser.data.type;

import me.supcheg.sanparser.id.SantechIdentifier;

public class SantechIdentifierType extends StringConvertingType<SantechIdentifier> {
    @Override
    public SantechIdentifier fromStringValue(CharSequence sequence) {
        return SantechIdentifier.fromString(sequence.toString());
    }
}

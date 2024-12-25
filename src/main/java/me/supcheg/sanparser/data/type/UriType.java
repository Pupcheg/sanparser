package me.supcheg.sanparser.data.type;

import java.net.URI;

public class UriType extends StringConvertingType<URI> {
    @Override
    public URI fromStringValue(CharSequence sequence) {
        return URI.create(sequence.toString());
    }
}

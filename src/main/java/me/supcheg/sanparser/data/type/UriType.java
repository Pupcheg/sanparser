package me.supcheg.sanparser.data.type;

import java.net.URI;

class UriType extends StringConvertingImmutableType<URI> {
    @Override
    public URI fromStringValue(CharSequence sequence) {
        return URI.create(sequence.toString());
    }

    @Override
    public Class<URI> returnedClass() {
        return URI.class;
    }
}

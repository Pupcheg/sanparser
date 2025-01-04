package me.supcheg.sanparser.santech.uri;

import java.net.URI;
import java.util.stream.Stream;

public interface SantechUriSource {
    Stream<URI> uris();

    long size();
}

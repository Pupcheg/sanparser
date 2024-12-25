package me.supcheg.sanparser.uri;

import java.net.URI;
import java.util.stream.Stream;

public interface SantechUriSource {
    Stream<URI> uris();

    long size();
}

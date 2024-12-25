package me.supcheg.sanparser.uri.recognizer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

public interface UriRecognizer {

    boolean canRecognize(URI uri);

    Optional<InputStream> recognize(URI uri) throws IOException;
}

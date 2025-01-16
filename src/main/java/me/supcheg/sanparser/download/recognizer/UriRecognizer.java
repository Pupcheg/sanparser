package me.supcheg.sanparser.download.recognizer;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

public interface UriRecognizer {

    boolean canRecognize(URI uri);

    Optional<InputStream> recognize(URI uri);
}

package me.supcheg.sanparser.download;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

public interface UriDownloader {
    Optional<InputStream> download(URI uri);
}

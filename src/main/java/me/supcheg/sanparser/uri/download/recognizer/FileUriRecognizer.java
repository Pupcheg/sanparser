package me.supcheg.sanparser.uri.download.recognizer;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Recognizer
@Component
class FileUriRecognizer implements UriRecognizer {

    @Override
    public boolean canRecognize(URI uri) {
        return uri.getScheme().equals("file");
    }

    @Override
    public Optional<InputStream> recognize(URI uri) throws IOException {
        Path path = Path.of(uri);
        return Files.notExists(path) ?
                Optional.empty() :
                Optional.of(Files.newInputStream(path));
    }
}

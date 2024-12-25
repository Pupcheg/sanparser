package me.supcheg.sanparser.uri.recognizer;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@Component
public class WebUriRecognizer implements UriRecognizer {
    @Override
    public boolean canRecognize(URI uri) {
        return uri.getScheme().equals("http") || uri.getScheme().equals("https");
    }

    @Override
    public Optional<InputStream> recognize(URI uri) throws IOException {
        try {
            return Optional.of(uri.toURL().openStream());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
    }
}

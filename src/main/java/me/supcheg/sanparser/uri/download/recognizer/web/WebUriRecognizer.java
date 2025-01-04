package me.supcheg.sanparser.uri.download.recognizer.web;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.download.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class WebUriRecognizer implements UriRecognizer {
    private final RestClient client;

    @Override
    public boolean canRecognize(URI uri) {
        return uri.getScheme().equals("http") || uri.getScheme().equals("https");
    }

    @Override
    public Optional<InputStream> recognize(URI uri) throws IOException {
        try {
            return Optional.ofNullable(
                    client.get()
                            .uri(uri)
                            .exchange((request, response) -> response.getBody(), false)
            );
        } catch (ResourceAccessException ex) {
            Throwable cause = ex.getCause();

            if (cause == null) {
                throw ex;
            }

            if (cause instanceof FileNotFoundException) {
                return Optional.empty();
            }

            throw (IOException) cause; // ResourceAccessException always have IOException as cause
        }
    }
}

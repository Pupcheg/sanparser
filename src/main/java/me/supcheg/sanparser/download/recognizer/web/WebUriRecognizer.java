package me.supcheg.sanparser.download.recognizer.web;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.download.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

import static lombok.Lombok.sneakyThrow;

@RequiredArgsConstructor
@Component
class WebUriRecognizer implements UriRecognizer {
    private final RestClient client;

    @Override
    public boolean canRecognize(URI uri) {
        return uri.getScheme().equals("http") || uri.getScheme().equals("https");
    }

    @Override
    public Optional<InputStream> recognize(URI uri) {
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

            throw sneakyThrow(cause); // lombok annotation processor will wrap this call and no runtime dependency will be provided
        }
    }
}

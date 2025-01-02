package me.supcheg.sanparser.uri.recognizer.web;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.uri.recognizer.UriRecognizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WebUriRecognizer implements UriRecognizer {
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
        } catch (ResourceAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}

package me.supcheg.sanparser.uri.recognizer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class WebUriRecognizer implements UriRecognizer {
    private final WebClient webClient;

    @Override
    public Flux<DataBuffer> recognize(URI uri) throws UnsupportedUriException {
        try {
            return webClient.get()
                    .uri(uri)
                    .exchangeToFlux(clientResponse -> clientResponse
                            .body((httpResponse, ctx) ->
                                    httpResponse.getBody()
                            ));
        } catch (Exception e) {
            throw new UnsupportedUriException(e);
        }
    }
}

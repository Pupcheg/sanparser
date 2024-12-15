package me.supcheg.sanparser.uri.recognizer;

import lombok.experimental.StandardException;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

import java.net.URI;

public interface UriRecognizer {

    Flux<DataBuffer> recognize(URI uri) throws UnsupportedUriException;

    @StandardException
    class UnsupportedUriException extends Exception {
    }
}

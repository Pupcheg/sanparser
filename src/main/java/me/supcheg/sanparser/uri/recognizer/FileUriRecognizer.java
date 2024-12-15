package me.supcheg.sanparser.uri.recognizer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

@RequiredArgsConstructor
@Component
public class FileUriRecognizer implements UriRecognizer {
    private final DataBufferFactory dataBufferFactory;

    @Override
    public Flux<DataBuffer> recognize(URI uri) throws UnsupportedUriException {
        Path path;

        try {
            path = Path.of(uri);
        } catch (FileSystemNotFoundException e) {
            throw new UnsupportedUriException(e);
        }

        return DataBufferUtils.read(path, dataBufferFactory, 1024);
    }
}

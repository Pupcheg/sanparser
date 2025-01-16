package me.supcheg.sanparser.download.recognizer;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.pivovarit.function.ThrowingFunction.sneaky;

@Recognizer
@Component
class FileUriRecognizer implements UriRecognizer {

    @Override
    public boolean canRecognize(URI uri) {
        return uri.getScheme().equals("file");
    }

    @Override
    public Optional<InputStream> recognize(URI uri) {
        return Optional.of(uri)
                .map(Path::of)
                .filter(Files::exists)
                .map(sneaky(Files::newInputStream));
    }
}

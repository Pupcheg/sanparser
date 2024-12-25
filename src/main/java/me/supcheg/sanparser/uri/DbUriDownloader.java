package me.supcheg.sanparser.uri;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.data.url.DownloadedUrl;
import me.supcheg.sanparser.data.url.DownloadedUrlRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Primary
public class DbUriDownloader implements UriDownloader {
    private final UriDownloader delegate;
    private final DownloadedUrlRepository downloadedUrlRepository;

    @SneakyThrows
    @Override
    public Optional<InputStream> download(URI uri) {
        DownloadedUrl downloadedUrl = downloadedUrlRepository.findById(uri).orElse(null);

        if (downloadedUrl != null) {
            return optionalInputStream(downloadedUrl.getData());
        }

        byte[] bytes;
        try (InputStream in = delegate.download(uri).orElse(null)) {
            bytes = in == null ? null : in.readAllBytes();
        }

        downloadedUrlRepository.save(new DownloadedUrl(uri, bytes));

        return optionalInputStream(bytes);
    }

    private Optional<InputStream> optionalInputStream(byte[] bytes) {
        return Optional.ofNullable(bytes)
                .map(ByteArrayInputStream::new);
    }
}

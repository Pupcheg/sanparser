package me.supcheg.sanparser.download;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.supcheg.sanparser.data.url.DownloadedUrl;
import me.supcheg.sanparser.data.url.DownloadedUrlRepository;
import org.hibernate.engine.jdbc.proxy.BlobProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;
import java.util.Optional;

import static com.pivovarit.function.ThrowingFunction.sneaky;

@Slf4j
@RequiredArgsConstructor
@Component
@Primary
class DbUriDownloader implements UriDownloader {
    private final UriDownloader delegate;
    private final DownloadedUrlRepository downloadedUrlRepository;

    @Override
    public Optional<InputStream> download(URI uri) {
        Optional<DownloadedUrl> downloadedUrl = downloadedUrlRepository.findById(uri);

        if (downloadedUrl.isPresent()) {
            return downloadedUrl
                    .map(DownloadedUrl::getData)
                    .map(sneaky(Blob::getBinaryStream));
        }

        Optional<byte[]> bytes = delegate.download(uri)
                .map(this::downloadInputStream);

        downloadedUrlRepository.save(
                DownloadedUrl.builder()
                        .url(uri)
                        .data(
                                bytes.map(BlobProxy::generateProxy)
                                        .orElse(null)
                        )
                        .build()
        );

        return bytes
                .map(ByteArrayInputStream::new);
    }

    @SneakyThrows
    private byte[] downloadInputStream(InputStream in) {
        try (in) {
            return in.readAllBytes();
        }
    }
}

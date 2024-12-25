package me.supcheg.sanparser.data.url;

import org.springframework.data.repository.CrudRepository;

import java.net.URI;

public interface DownloadedUrlRepository extends CrudRepository<DownloadedUrl, URI> {
}

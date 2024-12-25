package me.supcheg.sanparser.data.uri;

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URI;

public interface IdentifiedUriRepository extends JpaRepository<IdentifiedUri, URI> {
}

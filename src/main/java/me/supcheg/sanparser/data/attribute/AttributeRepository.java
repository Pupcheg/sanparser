package me.supcheg.sanparser.data.attribute;

import org.springframework.data.repository.CrudRepository;

import java.net.URI;
import java.util.Optional;

public interface AttributeRepository extends CrudRepository<AttributeCache, URI> {
    Optional<AttributeCache> findByUriAndAttributeKey(URI uri, String attributeKey);
}

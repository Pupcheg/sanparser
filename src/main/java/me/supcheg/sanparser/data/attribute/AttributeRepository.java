package me.supcheg.sanparser.data.attribute;

import org.springframework.data.repository.CrudRepository;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface AttributeRepository extends CrudRepository<AttributeCache, UUID> {
    Optional<AttributeCache> findByUriAndAttributeKey(URI uri, String attributeKey);
}

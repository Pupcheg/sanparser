package me.supcheg.sanparser.data.attribute;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.net.URI;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeCache {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private URI uri;

    @Column(nullable = false)
    private String attributeKey;

    private String jsonValue;
}

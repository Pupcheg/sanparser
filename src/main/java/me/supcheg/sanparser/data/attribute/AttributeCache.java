package me.supcheg.sanparser.data.attribute;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.supcheg.sanparser.data.type.UriType;
import org.hibernate.annotations.Type;
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

    @Type(UriType.class)
    private URI uri;

    private String attributeKey;

    private String jsonValue;
}

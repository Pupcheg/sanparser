package me.supcheg.sanparser.data.uri;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.supcheg.sanparser.data.type.SantechIdentifierType;
import me.supcheg.sanparser.data.type.UriType;
import me.supcheg.sanparser.id.SantechIdentifier;
import org.hibernate.annotations.Type;

import java.net.URI;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifiedUri {
    @Id
    @Type(UriType.class)
    private URI uri;

    @Type(SantechIdentifierType.class)
    private SantechIdentifier santechIdentifier;
}

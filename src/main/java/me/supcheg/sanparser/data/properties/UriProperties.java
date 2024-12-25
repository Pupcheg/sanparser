package me.supcheg.sanparser.data.properties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.supcheg.sanparser.data.type.StringStringMapType;
import me.supcheg.sanparser.data.type.UriType;
import org.hibernate.annotations.Type;

import java.net.URI;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UriProperties {
    @Id
    @Type(UriType.class)
    private URI uri;

    @Type(StringStringMapType.class)
    private Map<String, String> properties;
}

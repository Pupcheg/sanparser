package me.supcheg.sanparser.data.url;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.supcheg.sanparser.data.type.UriType;
import org.hibernate.annotations.Type;

import java.net.URI;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadedUrl {
    @Id
    @Type(UriType.class)
    private URI url;

    private byte[] data;
}

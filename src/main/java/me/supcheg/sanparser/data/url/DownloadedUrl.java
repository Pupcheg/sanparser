package me.supcheg.sanparser.data.url;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadedUrl {
    @Id
    private URI url;

    private byte[] data;
}

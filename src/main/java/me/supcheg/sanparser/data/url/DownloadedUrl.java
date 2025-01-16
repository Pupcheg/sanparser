package me.supcheg.sanparser.data.url;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.sql.Blob;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadedUrl {
    @Id
    private URI url;

    @Lob
    private Blob data;
}

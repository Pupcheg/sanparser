package me.supcheg.sanparser.data;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class IdentifiedUri {
    @Id
    private String uri;

    private String nomenclatureNumber;

    private String variantId;

    private String itemId;
}

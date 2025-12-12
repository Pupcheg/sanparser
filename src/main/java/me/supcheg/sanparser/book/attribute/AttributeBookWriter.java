package me.supcheg.sanparser.book.attribute;

import me.supcheg.sanparser.book.BookWriter;
import me.supcheg.sanparser.santech.SantechCategory;
import me.supcheg.sanparser.santech.SantechIdentifier;

import java.util.List;
import java.util.Map;

public interface AttributeBookWriter extends BookWriter {
    void append(AttributeBookEntry entry);

    record AttributeBookEntry(
            SantechCategory category,
            SantechIdentifier item,
            Map<String, String> properties
    ) {
    }

    record CategoryWithProperties(
            SantechCategory category,
            List<String> properties
    ) {
    }
}

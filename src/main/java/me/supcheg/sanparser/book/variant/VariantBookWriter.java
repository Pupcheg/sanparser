package me.supcheg.sanparser.book.variant;

import me.supcheg.sanparser.book.BookWriter;
import me.supcheg.sanparser.santech.LocalIdentifier;
import me.supcheg.sanparser.santech.SantechIdentifier;

import java.util.List;

public interface VariantBookWriter extends BookWriter {
    void append(VariantBookEntry entry);

    record VariantBookEntry(
            SantechIdentifier key,
            List<LocalIdentifier> associations
    ) {
    }
}

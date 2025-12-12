package me.supcheg.sanparser.book.variant;

public interface VariantBookWriterFactory {
    VariantBookWriter newWriter(int associationsAmount);
}

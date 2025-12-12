package me.supcheg.sanparser.book.attribute;

import me.supcheg.sanparser.book.attribute.AttributeBookWriter.CategoryWithProperties;

import java.util.List;

public interface AttributeBookWriterFactory {
    AttributeBookWriter newWriter(List<CategoryWithProperties> categories);
}

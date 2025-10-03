package me.supcheg.sanparser.book;

import java.util.Collection;
import java.util.Map;

public interface AttributeBookWriterFactory {
    AttributeBookWriter newWriter(Map<String, ? extends Collection<String>> availablePropertiesForGroup);
}

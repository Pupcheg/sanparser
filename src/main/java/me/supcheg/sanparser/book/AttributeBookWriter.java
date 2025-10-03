package me.supcheg.sanparser.book;

import me.supcheg.sanparser.santech.SantechIdentifier;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public interface AttributeBookWriter extends Closeable {
    void put(String group, SantechIdentifier identifier, Map<String, String> values);

    void save(Path outPath) throws IOException;
}

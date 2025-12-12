package me.supcheg.sanparser.book;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

public interface BookWriter extends Closeable {
    void save(Path path) throws IOException;
}

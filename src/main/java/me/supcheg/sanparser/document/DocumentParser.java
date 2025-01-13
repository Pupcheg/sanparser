package me.supcheg.sanparser.document;

import org.jsoup.nodes.Document;

import java.io.InputStream;

public interface DocumentParser {
    Document parse(InputStream in);

    Document parse(String string);
}

package me.supcheg.sanparser.properties;

import java.util.stream.Stream;

public interface ItemWithPropertySource {
    Stream<ItemWithProperties> items();
}

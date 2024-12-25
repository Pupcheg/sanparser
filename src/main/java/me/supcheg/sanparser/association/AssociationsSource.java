package me.supcheg.sanparser.association;

import java.util.stream.Stream;

public interface AssociationsSource {
    Stream<AssociatedItem> associations();
}

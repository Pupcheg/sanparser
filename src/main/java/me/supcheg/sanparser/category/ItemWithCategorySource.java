package me.supcheg.sanparser.category;

import java.util.stream.Stream;

public interface ItemWithCategorySource {
    Stream<ItemWithCategory> items();
}

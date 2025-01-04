package me.supcheg.sanparser.santech.repository;

import me.supcheg.sanparser.santech.SantechItem;

import java.net.URI;

public interface SantechItemRepository {
    SantechItem item(URI uri);
}

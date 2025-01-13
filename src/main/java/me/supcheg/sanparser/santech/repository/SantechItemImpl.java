package me.supcheg.sanparser.santech.repository;

import me.supcheg.sanparser.santech.SantechItem;

import java.net.URI;

record SantechItemImpl(
        URI uri
) implements SantechItem {
}

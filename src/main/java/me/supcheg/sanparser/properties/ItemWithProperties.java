package me.supcheg.sanparser.properties;

import me.supcheg.sanparser.id.SantechIdentifier;

import java.util.Map;

public record ItemWithProperties(
        SantechIdentifier id,
        Map<String, String> properties
){
}

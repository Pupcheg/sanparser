package me.supcheg.sanparser.association;

import me.supcheg.sanparser.id.SantechIdentifier;

import java.util.List;

public interface AssociationsResolver {
    default AssociatedItem resolveAssociations(SantechIdentifier root) {
        return new AssociatedItem(
                root,
                analogues(root),
                associations(root)
        );
    }

    List<SantechIdentifier> analogues(SantechIdentifier root);

    List<SantechIdentifier> associations(SantechIdentifier root);
}

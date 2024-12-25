package me.supcheg.sanparser.data.associations;

import me.supcheg.sanparser.id.SantechIdentifier;
import org.springframework.data.repository.CrudRepository;

public interface AssociationsRepository extends CrudRepository<Associations, SantechIdentifier> {
}

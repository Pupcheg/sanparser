package me.supcheg.sanparser.data.analogues;

import me.supcheg.sanparser.id.SantechIdentifier;
import org.springframework.data.repository.CrudRepository;

public interface AnaloguesRepository extends CrudRepository<Analogues, SantechIdentifier> {
}

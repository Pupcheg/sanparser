package me.supcheg.sanparser.association;

import lombok.RequiredArgsConstructor;
import me.supcheg.sanparser.data.analogues.Analogues;
import me.supcheg.sanparser.data.analogues.AnaloguesRepository;
import me.supcheg.sanparser.data.associations.Associations;
import me.supcheg.sanparser.data.associations.AssociationsRepository;
import me.supcheg.sanparser.id.SantechIdentifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@Primary
public class DbAssociationsResolver implements AssociationsResolver {
    private final AssociationsResolver delegate;
    private final AssociationsRepository associationsRepository;
    private final AnaloguesRepository analoguesRepository;

    @Override
    public List<SantechIdentifier> analogues(SantechIdentifier root) {
        return analoguesRepository.findById(root)
                .map(Analogues::getAnalogues)
                .map(Arrays::asList)
                .orElseGet(() -> {
                    List<SantechIdentifier> analogues = delegate.analogues(root);
                    analoguesRepository.save(new Analogues(root, analogues.toArray(SantechIdentifier[]::new)));
                    return analogues;
                });
    }

    @Override
    public List<SantechIdentifier> associations(SantechIdentifier root) {
        return associationsRepository.findById(root)
                .map(Associations::getAssociations)
                .map(Arrays::asList)
                .orElseGet(() -> {
                    List<SantechIdentifier> associations = delegate.associations(root);
                    associationsRepository.save(new Associations(root, associations.toArray(SantechIdentifier[]::new)));
                    return associations;
                });
    }
}

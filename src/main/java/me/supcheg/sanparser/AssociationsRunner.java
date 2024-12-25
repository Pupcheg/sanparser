package me.supcheg.sanparser;

import me.supcheg.sanparser.association.AssociationsSource;
import me.supcheg.sanparser.runner.ProgressBarRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        prefix = "sanparser",
        name = "mode",
        havingValue = "associations"
)
@Component
public class AssociationsRunner extends ProgressBarRunner {
    public AssociationsRunner(AssociationsSource associationsSource) {
        super("Associations", associationsSource::associations);
    }
}

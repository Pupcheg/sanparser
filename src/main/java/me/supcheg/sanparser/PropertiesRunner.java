package me.supcheg.sanparser;

import me.supcheg.sanparser.properties.ItemWithPropertySource;
import me.supcheg.sanparser.runner.ProgressBarRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        prefix = "sanparser",
        name = "mode",
        havingValue = "properties"
)
@Component
public class PropertiesRunner extends ProgressBarRunner {
    public PropertiesRunner(ItemWithPropertySource itemWithPropertySource) {
        super("Properties", itemWithPropertySource::items);
    }
}

package me.supcheg.sanparser;

import me.supcheg.sanparser.category.ItemWithCategorySource;
import me.supcheg.sanparser.runner.ProgressBarRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
        prefix = "sanparser",
        name = "mode",
        havingValue = "categories"
)
@Component
public class CategoriesRunner extends ProgressBarRunner {
    public CategoriesRunner(ItemWithCategorySource itemWithCategorySource) {
        super("Categories", itemWithCategorySource::items);
    }
}

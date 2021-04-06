package categories.category.event;

import categories.category.vo.CategoryId;
import java.util.UUID;

public class TopCategoryChangedEvent extends CategoryEvent {

    private final CategoryId topCategory;

    public TopCategoryChangedEvent(UUID categoryId, CategoryId topCategory) {
        super(categoryId);
        this.topCategory = topCategory;
    }

    public CategoryId getTopCategory() {
        return topCategory;
    }
}

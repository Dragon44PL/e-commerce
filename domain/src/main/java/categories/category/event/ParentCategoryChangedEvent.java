package categories.category.event;

import categories.category.vo.CategoryId;
import java.util.UUID;

public class ParentCategoryChangedEvent extends CategoryEvent {

    private final CategoryId parentCategory;

    public ParentCategoryChangedEvent(UUID categoryId, CategoryId parentCategory) {
        super(categoryId);
        this.parentCategory = parentCategory;
    }

    public CategoryId getParentCategory() {
        return parentCategory;
    }
}

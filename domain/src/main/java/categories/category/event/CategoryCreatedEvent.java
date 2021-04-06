package categories.category.event;

import categories.category.vo.CategoryId;
import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

    private final String name;
    private final CategoryId categoryId;

    public CategoryCreatedEvent(UUID categoryId, String name, CategoryId categoryId1) {
        super(categoryId);
        this.name = name;
        this.categoryId = categoryId1;
    }

    public String getName() {
        return name;
    }

    public CategoryId getCategory() {
        return categoryId;
    }

}

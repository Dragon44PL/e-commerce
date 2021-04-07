package categories.category;

import categories.category.event.CategoryCreatedEvent;
import categories.category.event.CategoryEvent;
import categories.category.event.ParentCategoryChangedEvent;
import categories.category.exception.ParentCategoryIdException;
import categories.category.vo.CategoryId;
import categories.category.vo.CategorySnapshot;
import domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Category extends AggregateRoot<UUID, CategorySnapshot, CategoryEvent> {

    private final UUID id;
    private final String name;
    private CategoryId parentCategory;

    static Category create(UUID id, String name, CategoryId topCategory) {
        final Category category = new Category(id, name, topCategory, new ArrayList<>());
        category.registerEvent(new CategoryCreatedEvent(id, name, topCategory));
        return category;
    }

    static Category create(UUID id, String name) {
        return Category.create(id, name, null);
    }

    private Category(UUID id, String name, CategoryId parentCategory, List<CategoryEvent> categoryEvents) {
        super(categoryEvents);
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }

    boolean sameCategory(CategoryId topCategory) {
        return id.compareTo(topCategory.id()) == 0;
    }

    boolean hasCategoryParent() {
        return parentCategory != null;
    }

    void changeParentCategory(CategoryId topCategory) throws ParentCategoryIdException {
        if(sameCategory(topCategory)) {
            throw new ParentCategoryIdException();
        }

        this.parentCategory = topCategory;
        registerEvent(new ParentCategoryChangedEvent(id, topCategory));
    }

    @Override
    public CategorySnapshot getSnapshot() {
        return new CategorySnapshot(id, name, parentCategory, new ArrayList<>(events()));
    }
}

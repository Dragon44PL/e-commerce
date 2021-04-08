package products.category;

import products.category.event.CategoryCreatedEvent;
import products.category.event.CategoryEvent;
import products.category.event.ParentCategoryChangedEvent;
import products.category.exception.ParentCategoryIdException;
import products.category.vo.CategoryId;
import products.category.vo.CategorySnapshot;
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

    static Category restore(CategorySnapshot categorySnapshot) {
        return new Category(categorySnapshot.id(), categorySnapshot.name(), categorySnapshot.parentCategory(), new ArrayList<>());
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

package org.ecommerce.shop.products.category;

import org.ecommerce.shop.products.category.event.CategoryCreatedEvent;
import org.ecommerce.shop.products.category.event.CategoryEvent;
import org.ecommerce.shop.products.category.event.CategoryNameChangedEvent;
import org.ecommerce.shop.products.category.event.ParentCategoryChangedEvent;
import org.ecommerce.shop.products.category.vo.CategoryId;
import org.ecommerce.shop.core.AggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Category extends AggregateRoot<UUID, CategoryEvent> {

    private final UUID id;
    private String name;
    private CategoryId parentCategory;

    static Category create(UUID id, String name, CategoryId topCategory) {
        final Category category = new Category(id, name, topCategory, new ArrayList<>());
        category.registerEvent(new CategoryCreatedEvent(id, name, topCategory));
        return category;
    }

    static Category create(UUID id, String name) {
        return Category.create(id, name, null);
    }

    static Category restore(UUID id, String name, CategoryId parentCategory) {
        return new Category(id, name, parentCategory, new ArrayList<>());
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

    void changeParentCategory(CategoryId parentCategory) {
        if(!sameCategory(parentCategory)) {
            this.parentCategory = parentCategory;
            final ParentCategoryChangedEvent parentCategoryChangedEvent = new ParentCategoryChangedEvent(id, this.parentCategory);
            registerEvent(parentCategoryChangedEvent);
        }
    }

    void changeCategoryName(String candidate) {
        if(!sameCategoryName(candidate)) {
            this.name = candidate;
            final CategoryNameChangedEvent categoryNameChangedEvent = new CategoryNameChangedEvent(id, this.name);
            this.registerEvent(categoryNameChangedEvent);
        }
    }

    private boolean sameCategoryName(String candidate) {
        return name.equals(candidate);
    }
}

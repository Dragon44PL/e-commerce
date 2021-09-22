package org.ecommerce.shop.products.category;

import org.ecommerce.shop.products.category.event.CategoryCreatedEvent;
import org.ecommerce.shop.products.category.event.CategoryEvent;
import org.ecommerce.shop.products.category.event.CategoryNameChangedEvent;
import org.ecommerce.shop.products.category.event.ParentCategoryChangedEvent;
import org.ecommerce.shop.products.category.exception.ParentCategoryIdException;
import org.ecommerce.shop.products.category.vo.CategoryId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private final UUID PARENT_CATEGORY_ID = UUID.randomUUID();
    private final CategoryId PARENT_CATEGORY = new CategoryId(PARENT_CATEGORY_ID);

    private final UUID ANOTHER_PARENT_CATEGORY_ID = UUID.randomUUID();
    private final CategoryId ANOTHER_PARENT_CATEGORY = new CategoryId(ANOTHER_PARENT_CATEGORY_ID);

    private final UUID CATEGORY_ID = UUID.randomUUID();
    private final String CATEGORY_NAME = "NAME";

    private final String ANOTHER_CATEGORY_NAME = "ANOTHER NAME";

    private final Class<CategoryCreatedEvent> CATEGORY_CREATED_EVENT = CategoryCreatedEvent.class;
    private final Class<ParentCategoryChangedEvent> PARENT_CATEGORY_CHANGED_EVENT = ParentCategoryChangedEvent.class;
    private final Class<CategoryNameChangedEvent> CATEGORY_NAME_CHANGED_EVENT = CategoryNameChangedEvent.class;

    private final Class<ParentCategoryIdException> PARENT_CATEGORY_ID_EXCEPTION = ParentCategoryIdException.class;

    @Test
    @DisplayName("Category Creation Should Create CategoryCreateEvent Properly")
    void categoryCreationShouldCreateEvent() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);

        final Optional<CategoryEvent> categoryEvent = category.findLatestEvent();
        assertTrue(categoryEvent.isPresent());
        assertEquals(categoryEvent.get().getClass(), CATEGORY_CREATED_EVENT);

        final CategoryCreatedEvent categoryCreatedEvent = (CategoryCreatedEvent) categoryEvent.get();
        assertEquals(categoryCreatedEvent.parentCategory(), PARENT_CATEGORY);
        assertEquals(categoryCreatedEvent.name(), CATEGORY_NAME);
        assertEquals(categoryCreatedEvent.aggregateId(), CATEGORY_ID);
    }

    @Test
    @DisplayName("Both Categories Should Have Same ID")
    void categoriesShouldHasSameId() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        assertTrue(category.sameCategory(new CategoryId(CATEGORY_ID)));
    }

    @Test
    @DisplayName("Both Categories Should Not Have Same ID")
    void categoriesShouldNotHaveSameId() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        assertFalse(category.sameCategory(new CategoryId(PARENT_CATEGORY_ID)));
    }

    @Test
    @DisplayName("Category Should Have Parent")
    void categoryShouldHaveParent() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        assertTrue(category.hasCategoryParent());
    }

    @Test
    @DisplayName("Category Should Not Have Parent")
    void categoryShouldNotHaveParent() {
        final Category another = Category.create(CATEGORY_ID, CATEGORY_NAME);
        assertFalse(another.hasCategoryParent());
    }

    @Test
    @DisplayName("Category Should Change Parent")
    void categoryShouldChangeParent() {
        final Category category = Category.restore(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        category.changeParentCategory(ANOTHER_PARENT_CATEGORY);

        final Optional<CategoryEvent> categoryEvent = category.findLatestEvent();
        assertTrue(categoryEvent.isPresent());
        assertEquals(categoryEvent.get().getClass(), PARENT_CATEGORY_CHANGED_EVENT);

        final ParentCategoryChangedEvent parentCategoryChangedEvent = (ParentCategoryChangedEvent) categoryEvent.get();
        assertEquals(parentCategoryChangedEvent.parentCategory(), ANOTHER_PARENT_CATEGORY);
    }

    @Test
    @DisplayName("Category Should Throw ParentCategoryIdException")
    void categoryShouldThrowParentCategoryIdException() {
        final Category category = Category.restore(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        assertThrows(PARENT_CATEGORY_ID_EXCEPTION, () -> category.changeParentCategory(new CategoryId(CATEGORY_ID)));
    }

    @Test
    @DisplayName("Category Name Should Change And Generate CategoryNameChangedEvent")
    void categoryNameShouldChangeAndGenerateEvent() {
        final Category category = Category.restore(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        category.changeCategoryName(ANOTHER_CATEGORY_NAME);

        final Optional<CategoryEvent> categoryEvent = category.findLatestEvent();
        assertTrue(categoryEvent.isPresent());
        assertEquals(categoryEvent.get().getClass(), CATEGORY_NAME_CHANGED_EVENT);

        final CategoryNameChangedEvent categoryNameChangedEvent = (CategoryNameChangedEvent) categoryEvent.get();
        assertEquals(categoryNameChangedEvent.name(), ANOTHER_CATEGORY_NAME);
        assertEquals(categoryNameChangedEvent.aggregateId(), CATEGORY_ID);
    }

    @Test
    @DisplayName("Category Name Should Not Change When The Same")
    void categoryNameShouldNotChange() {
        final Category category = Category.restore(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        category.changeCategoryName(CATEGORY_NAME);

        final Optional<CategoryEvent> categoryEvent = category.findLatestEvent();
        assertFalse(categoryEvent.isPresent());
    }
}

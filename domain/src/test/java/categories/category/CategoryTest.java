package categories.category;

import categories.category.event.CategoryCreatedEvent;
import categories.category.event.CategoryEvent;
import categories.category.event.ParentCategoryChangedEvent;
import categories.category.exception.ParentCategoryIdException;
import categories.category.vo.CategoryId;
import categories.category.vo.CategorySnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
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

    private final Class<CategoryCreatedEvent> CATEGORY_CREATED_EVENT = CategoryCreatedEvent.class;
    private final Class<ParentCategoryChangedEvent> PARENT_CATEGORY_CHANGED_EVENT = ParentCategoryChangedEvent.class;

    private final Class<ParentCategoryIdException> PARENT_CATEGORY_ID_EXCEPTION = ParentCategoryIdException.class;

    @Test
    @DisplayName("Category Creation Should Create CategorySnapshot Properly")
    void categoryCreationShouldCreateProperly() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        final CategorySnapshot categorySnapshot = category.getSnapshot();

        assertEquals(categorySnapshot.id(), CATEGORY_ID);
        assertEquals(categorySnapshot.name(), CATEGORY_NAME);
        assertEquals(categorySnapshot.parentCategory(), PARENT_CATEGORY);
    }

    @Test
    @DisplayName("Category Creation Should Create CategoryCreateEvent Properly")
    void categoryCreationShouldCreateEvent() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        final CategorySnapshot categorySnapshot = category.getSnapshot();
        final List<CategoryEvent> categoryEvents = categorySnapshot.events();
        assertNotNull(categoryEvents);
        assertFalse(categoryEvents.isEmpty());

        final CategoryEvent categoryEvent = categoryEvents.stream().findFirst().get();
        assertEquals(categoryEvent.getClass(), CATEGORY_CREATED_EVENT);

        final CategoryCreatedEvent categoryCreatedEvent = (CategoryCreatedEvent) categoryEvent;
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
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        category.changeParentCategory(ANOTHER_PARENT_CATEGORY);
        final CategorySnapshot categorySnapshot = category.getSnapshot();
        final List<CategoryEvent> categoryEvents = categorySnapshot.events();
        assertNotNull(categoryEvents);
        assertFalse(categoryEvents.isEmpty());

        final Optional<CategoryEvent> categoryEvent = categorySnapshot.findLatestEvent();
        assertTrue(categoryEvent.isPresent());
        assertEquals(categoryEvent.get().getClass(), PARENT_CATEGORY_CHANGED_EVENT);

        final ParentCategoryChangedEvent parentCategoryChangedEvent = (ParentCategoryChangedEvent) categoryEvent.get();
        assertEquals(parentCategoryChangedEvent.parentCategory(), ANOTHER_PARENT_CATEGORY);
    }

    @Test
    @DisplayName("Category Should Throw ParentCategoryIdException")
    void categoryShouldThrowParentCategoryIdException() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        assertThrows(PARENT_CATEGORY_ID_EXCEPTION, () -> category.changeParentCategory(new CategoryId(CATEGORY_ID)));
    }

    @Test
    @DisplayName("Modifying Category Snapshot Events Should Not Touch Category Events")
    void modifyingSnapshotEventsShouldNotTouchAggregateEvents() {
        final Category category = Category.create(CATEGORY_ID, CATEGORY_NAME, PARENT_CATEGORY);
        final List<CategoryEvent> modifiedCategoryEvents = category.getSnapshot().events();
        modifiedCategoryEvents.clear();

        final List<CategoryEvent> originalCategoryEvents = category.getSnapshot().events();
        assertNotEquals(modifiedCategoryEvents.size(), originalCategoryEvents.size());
    }
}

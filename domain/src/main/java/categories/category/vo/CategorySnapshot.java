package categories.category.vo;

import categories.category.event.CategoryEvent;
import domain.EntitySnapshot;
import java.util.List;
import java.util.UUID;

public record CategorySnapshot(UUID id, String name, CategoryId topCategory, List<CategoryEvent> categoryEvents) implements EntitySnapshot { }

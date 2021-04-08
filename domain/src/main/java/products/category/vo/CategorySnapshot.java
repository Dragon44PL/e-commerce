package products.category.vo;

import products.category.event.CategoryEvent;
import domain.vo.EventEntitySnapshot;

import java.util.List;
import java.util.UUID;

public record CategorySnapshot(UUID id, String name, CategoryId parentCategory, List<CategoryEvent> events) implements EventEntitySnapshot<CategoryEvent> { }

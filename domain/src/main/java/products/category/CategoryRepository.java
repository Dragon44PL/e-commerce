package products.category;

import domain.DomainRepository;
import java.util.UUID;

interface CategoryRepository extends DomainRepository<UUID, Category> { }

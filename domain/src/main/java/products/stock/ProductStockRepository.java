package products.stock;

import domain.DomainRepository;
import java.util.UUID;

interface ProductStockRepository extends DomainRepository<UUID, ProductStock> { }

package products.product;

import domain.DomainRepository;
import java.util.UUID;

interface ProductRepository extends DomainRepository<UUID, Product> { }

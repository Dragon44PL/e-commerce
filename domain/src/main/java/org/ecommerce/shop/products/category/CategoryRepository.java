package org.ecommerce.shop.products.category;

import org.ecommerce.shop.core.DomainRepository;
import java.util.UUID;

interface CategoryRepository extends DomainRepository<UUID, Category> { }

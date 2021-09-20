package org.ecommerce.shop.products.stock;

import org.ecommerce.shop.core.DomainRepository;
import java.util.UUID;

interface ProductStockRepository extends DomainRepository<UUID, ProductStock> { }

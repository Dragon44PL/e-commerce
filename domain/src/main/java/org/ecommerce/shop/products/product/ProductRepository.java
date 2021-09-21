package org.ecommerce.shop.products.product;

import org.ecommerce.shop.core.DomainRepository;
import java.util.UUID;

interface ProductRepository extends DomainRepository<UUID, Product> { }

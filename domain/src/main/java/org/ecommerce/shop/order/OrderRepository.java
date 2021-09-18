package org.ecommerce.shop.order;

import org.ecommerce.shop.core.DomainRepository;

import java.util.UUID;

interface OrderRepository extends DomainRepository<UUID, Order> { }
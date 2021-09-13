package org.ecommerce.shop.users.user;

import org.ecommerce.shop.core.DomainRepository;
import java.util.UUID;

interface UserRepository extends DomainRepository<UUID, User> { }

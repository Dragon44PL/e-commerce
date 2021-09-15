package org.ecommerce.shop.accounting.account;

import org.ecommerce.shop.core.DomainRepository;
import java.util.UUID;

interface AccountRepository extends DomainRepository<UUID, Account> { }

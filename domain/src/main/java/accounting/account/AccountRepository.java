package accounting.account;

import domain.DomainRepository;
import java.util.UUID;

interface AccountRepository extends DomainRepository<UUID, Account> { }

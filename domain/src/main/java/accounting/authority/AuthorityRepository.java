package accounting.authority;

import domain.DomainRepository;
import java.util.UUID;

interface AuthorityRepository extends DomainRepository<UUID, Authority> { }

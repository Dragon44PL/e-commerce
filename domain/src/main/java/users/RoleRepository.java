package users;

import domain.DomainRepository;
import java.util.UUID;

interface RoleRepository extends DomainRepository<UUID, Role> { }

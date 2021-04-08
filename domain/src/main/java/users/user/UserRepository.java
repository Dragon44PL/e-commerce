package users.user;

import domain.DomainRepository;
import java.util.UUID;

interface UserRepository extends DomainRepository<UUID, User> { }

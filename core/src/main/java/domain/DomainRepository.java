package domain;

import domain.vo.EntitySnapshot;

public interface DomainRepository<I, T extends Aggregate<I, ? extends EntitySnapshot>> {
    void deleteById(I id);
    T save(T t);
}

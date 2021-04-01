package domain;

public interface DomainRepository<I, T extends Aggregate<I, ? extends EntitySnapshot<I>>> {
    void deleteById(I id);
    T save(T t);
}

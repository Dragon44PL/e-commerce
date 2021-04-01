package domain;

public interface DomainEntity<I, T extends EntitySnapshot> {
    T getSnapshot();
}

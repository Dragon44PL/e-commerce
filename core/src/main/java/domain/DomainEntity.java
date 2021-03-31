package domain;

public interface DomainEntity<I, T extends EntitySnapshot<I>> {
    T getSnapshot();
}

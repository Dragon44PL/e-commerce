package domain;

public interface Aggregate<I, T extends EntitySnapshot<I>> extends DomainEntity<I, T> { }

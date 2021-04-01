package domain;

public interface Aggregate<I, T extends EntitySnapshot> extends DomainEntity<I, T> { }

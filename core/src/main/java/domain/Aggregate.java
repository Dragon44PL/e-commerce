package domain;

import domain.vo.EntitySnapshot;

public interface Aggregate<I, T extends EntitySnapshot> extends DomainEntity<I, T> { }

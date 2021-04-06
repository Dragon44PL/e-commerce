package domain;

import domain.vo.EntitySnapshot;

public interface DomainEntity<I, T extends EntitySnapshot> {
    T getSnapshot();
}

package org.ecommerce.shop.core;

public interface DomainRepository<I, T extends Aggregate> {
    void findById(I id);
    void delete(T t);
    T save(T t);
}
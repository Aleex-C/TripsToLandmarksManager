package org.example.repository;

import org.example.domain.Entity;

import java.util.Optional;

public interface IRepository <ID, E extends Entity<ID>> {
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    void save(E entity);
    void delete (ID id);
    void update(ID id, E entity);
}

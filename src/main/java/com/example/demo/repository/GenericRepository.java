package com.example.demo.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    void persist(T entity);

    void removeByID(ID id);

    List<T> findAll();

    T findById(ID id);

    void merge(T entity);

    void remove(T entity);
}

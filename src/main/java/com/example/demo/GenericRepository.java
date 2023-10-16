package com.example.demo;

import java.util.List;

public interface GenericRepository<T, ID> {
    void persist(T entity);

    void deleteById(ID id);

    List<T> findAll();

    T findById(ID id);

    void merge(T entity);
}

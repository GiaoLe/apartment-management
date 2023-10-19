package com.example.demo.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    //save
    void persist(T entity);

    void deleteById(ID id);

    List<T> findAll();

    T findById(ID id);

    //save or update
    void merge(T entity);
}

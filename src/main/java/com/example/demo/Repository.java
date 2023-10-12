package com.example.demo;

import jakarta.persistence.Entity;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

public abstract class Repository<T, ID> implements GenericRepository<T, ID> {

    private final Class<T> entityClass;

    public Repository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void persist(T entity) {
        HibernateUtility.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
        });
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        }
    }

    @Override
    public T findById(ID id) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            return session.find(entityClass, id);
        }
    }

    @Override
    public void merge(T entity) {
        HibernateUtility.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }
}

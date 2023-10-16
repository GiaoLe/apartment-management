package com.example.demo;

import java.lang.reflect.ParameterizedType;
import java.util.List;

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
    public List<T> findAll() {
        return HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from " + entityClass.getName(), entityClass).list());
    }

    @Override
    public T findById(ID id) {
        return HibernateUtility.getSessionFactory().fromTransaction(session -> session.find(entityClass, id));
    }

    @Override
    public void merge(T entity) {
        HibernateUtility.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
            session.flush();
        });
    }

    @Override
    public void deleteById(ID id) {
        HibernateUtility.getSessionFactory().inTransaction(session -> {
            T entity = session.find(entityClass, id);
            session.remove(entity);
        });
    }
}

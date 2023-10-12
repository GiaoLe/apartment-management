package com.example.demo;

import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class Repository<T, ID> implements GenericRepository<T, ID> {

    private final Class<T> entityClass;

    public Repository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void persist(T entity) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
        });
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        }
    }

    @Override
    public T findById(ID id) {
        return null;
    }
}

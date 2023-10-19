package com.example.demo.service;

import com.example.demo.dao.Collection;
import com.example.demo.repository.CollectionRepository;

import java.util.List;

public class CollectionService {
    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public void persist(Collection collection) {
        collectionRepository.persist(collection);
    }

    public List<Collection> findAll() {
        return collectionRepository.findAll();
    }
}

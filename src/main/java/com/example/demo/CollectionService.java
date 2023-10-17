package com.example.demo;

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

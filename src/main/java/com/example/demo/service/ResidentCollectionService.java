package com.example.demo.service;

import com.example.demo.dao.Collection;
import com.example.demo.dao.Resident;
import com.example.demo.dao.ResidentCollection;
import com.example.demo.repository.ResidentCollectionRepository;

public class ResidentCollectionService {
    private final ResidentCollectionRepository residentCollectionRepository;

    public ResidentCollectionService(ResidentCollectionRepository residentCollectionRepository) {
        this.residentCollectionRepository = residentCollectionRepository;
    }

    public void persist(Resident resident, Collection collection) {
        ResidentCollection residentCollection = new ResidentCollection(resident, collection);
        residentCollectionRepository.persist(residentCollection);
    }
}
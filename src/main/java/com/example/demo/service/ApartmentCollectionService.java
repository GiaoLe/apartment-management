package com.example.demo.service;

import com.example.demo.dao.ApartmentCollection;
import com.example.demo.repository.ApartmentCollectionRepository;

public class ApartmentCollectionService {
    private final ApartmentCollectionRepository apartmentCollectionRepository;

    public ApartmentCollectionService(ApartmentCollectionRepository apartmentCollectionRepository) {
        this.apartmentCollectionRepository = apartmentCollectionRepository;
    }

    public void persist(ApartmentCollection apartmentCollection) {
        apartmentCollectionRepository.persist(apartmentCollection);
    }

    public void merge(ApartmentCollection apartmentCollection) {
        apartmentCollectionRepository.merge(apartmentCollection);
    }
}
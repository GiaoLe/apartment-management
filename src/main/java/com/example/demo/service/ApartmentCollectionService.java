package com.example.demo.service;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Collection;
import com.example.demo.dao.Resident;
import com.example.demo.dao.ApartmentCollection;
import com.example.demo.repository.ApartmentCollectionRepository;

public class ApartmentCollectionService {
    private final ApartmentCollectionRepository apartmentCollectionRepository;

    public ApartmentCollectionService(ApartmentCollectionRepository apartmentCollectionRepository) {
        this.apartmentCollectionRepository = apartmentCollectionRepository;
    }

    public void persist(Apartment apartment, Collection collection) {
        ApartmentCollection apartmentCollection = new ApartmentCollection(apartment, collection);
        apartmentCollectionRepository.persist(apartmentCollection);
    }
}
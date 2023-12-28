package com.example.demo.service;

import com.example.demo.dao.ApartmentCollection;
import com.example.demo.repository.ApartmentCollectionRepository;

import java.util.List;

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
    public List<ApartmentCollection> findAll(){
        return apartmentCollectionRepository.findAll();
    }
    public void remove(ApartmentCollection apartmentCollection) {
        apartmentCollectionRepository.remove(apartmentCollection);
    }
}
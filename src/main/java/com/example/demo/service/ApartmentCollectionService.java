package com.example.demo.service;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Collection;
import com.example.demo.dao.Resident;
import com.example.demo.dao.ApartmentCollection;
import com.example.demo.repository.ApartmentCollectionRepository;

import java.util.Date;

public class ApartmentCollectionService {
    private final ApartmentCollectionRepository apartmentCollectionRepository;

    public ApartmentCollectionService(ApartmentCollectionRepository apartmentCollectionRepository) {
        this.apartmentCollectionRepository = apartmentCollectionRepository;
    }

    public void persist(Apartment apartment, Collection collection, Date deadlinePayment) {
        ApartmentCollection apartmentCollection = new ApartmentCollection(apartment, collection, deadlinePayment);
        apartmentCollectionRepository.persist(apartmentCollection);
    }
}
package com.example.demo.services;


import com.example.demo.dao.Apartment;
import com.example.demo.repositories.ApartmentRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public ApartmentService() {
        apartmentRepository = new ApartmentRepository();
    }
    public List<Apartment> findAll(){
        return  apartmentRepository.findAll();

    }
    public void persist(Apartment apartment) {
        apartmentRepository.persist(apartment);
    }

    public Apartment findByID(String apartmentID) {
        return apartmentRepository.findById(apartmentID);
    }
    public void merge(Apartment apartment){ apartmentRepository.merge(apartment);}
    public void remove(Apartment apartment){
        apartmentRepository.remove(apartment);
    }

    public Apartment findById(String id) {
        return apartmentRepository.findById(id);
    }
}

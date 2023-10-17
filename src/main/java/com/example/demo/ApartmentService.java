package com.example.demo;


import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public Apartment findByID(Integer id) {
        return apartmentRepository.findById(id);
    }

    public void merge(Apartment apartment) {
        apartmentRepository.merge(apartment);
    }

    public void persist(Apartment apartment) {
        apartmentRepository.persist(apartment);
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

}

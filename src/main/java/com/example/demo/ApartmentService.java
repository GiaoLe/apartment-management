package com.example.demo;


import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public void persist(Apartment apartment) {
        apartmentRepository.persist(apartment);
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

}

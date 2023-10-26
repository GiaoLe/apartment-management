package com.example.demo.service;


import com.example.demo.dao.Apartment;
import com.example.demo.repository.ApartmentRepository;
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

    public void remove(Apartment apartment) {
        apartmentRepository.remove(apartment);
    }
}

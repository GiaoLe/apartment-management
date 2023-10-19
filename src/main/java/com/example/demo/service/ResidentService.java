package com.example.demo.service;

import com.example.demo.model.Resident;
import com.example.demo.repository.ResidentRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ResidentService {
    private final ResidentRepository residentRepository;

    public void persist(Resident resident) {
        residentRepository.persist(resident);
    }

    public List<Resident> findAll() {
        return residentRepository.findAll();
    }

    public Resident findByID(Integer id) {
        return residentRepository.findById(id);
    }
}

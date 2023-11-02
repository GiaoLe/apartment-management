package com.example.demo.service;

import com.example.demo.dao.Resident;
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

    public void remove(Resident resident) {
        residentRepository.remove(resident);
    }

    public void merge(Resident resident) {
        residentRepository.merge(resident);
    }
}

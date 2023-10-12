package com.example.demo;

import java.util.List;

public class ResidentService {
    private final ResidentRepository residentRepository;

    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    public void save(Resident resident) {
        residentRepository.persist(resident);
    }

    public List<Resident> findAll() {
        return residentRepository.findAll();
    }
}

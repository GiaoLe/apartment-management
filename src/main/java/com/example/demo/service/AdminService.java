package com.example.demo.service;

import com.example.demo.dao.Admin;
import com.example.demo.repository.AdminRepository;

public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findByID(Integer id) {
        return adminRepository.findById(id);
    }
}

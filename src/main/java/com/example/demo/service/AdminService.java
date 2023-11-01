package com.example.demo.service;

import com.example.demo.dao.Admin;
import com.example.demo.repository.AdminRepository;

public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findByID(String id) {
        return adminRepository.findById(id);
    }

    public void merge(Admin admin) {
        adminRepository.merge(admin);
    }
}

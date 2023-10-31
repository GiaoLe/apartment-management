package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apartment {
    @Id
    @NotNull
    private String id;
    @NotNull
    private double area;
    @NotNull
    private String type;
    @NotNull
    private String status;
    @NotNull
    private int roomCount;
    //TODO find a better way to handle LAZY loading than using FetchType.EAGER
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Resident> residents = new ArrayList<>();

    public Apartment(String id, double area, int roomCount, String type, String status) {
        this.id = id;
        this.area = area;
        this.roomCount = roomCount;
        this.type = type;
        this.status = status;
    }

    public void addResident(Resident resident) {
        residents.add(resident);
    }

    public Integer getTotalResidents() {
        return residents.size();
    }

    @Transient
    public int getFloor() {
        return Integer.parseInt(id.substring(0, 1));
    }
}

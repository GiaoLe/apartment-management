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
    private ApartmentType type;
    @NotNull
    private ApartmentState state;
    @NotNull
    private int roomCount;
    //TODO find a better way to handle LAZY loading than using FetchType.EAGER
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Resident> residents = new ArrayList<>();

    public void addResident(Resident resident) {
        residents.add(resident);
        if (state == ApartmentState.AVAILABLE) {
            state = ApartmentState.OCCUPIED;
        }
    }

    @Transient
    public int getFloor() {
        return Integer.parseInt(id.substring(0, 1));
    }
}

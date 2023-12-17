package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @Getter
    @Setter
    private List<Resident> residents = new ArrayList<>();

    public void addResident(Resident resident) {
        residents.add(resident);
        if (state == ApartmentState.AVAILABLE) {
            state = ApartmentState.OCCUPIED;
        }
    }
    public Apartment(String id, double area, ApartmentType type, ApartmentState state, int roomCount){
        this.id = id;
        this.area = area;
        this.type = type;
        this.state = state;
        this.roomCount = roomCount;
    }
    @Transient
    public int getFloor() {
        return Integer.parseInt(id.substring(0, 1));
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Apartment{" +
                "id='" + id + '\'' +
                ", area=" + area +
                ", type=" + type +
                ", state=" + state +
                ", roomCount=" + roomCount +
                ", residents=[");

        for (Resident resident : residents) {
            sb.append(resident.toString()).append(", ");
        }

        if (!residents.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]}");

        return sb.toString();
    }
}

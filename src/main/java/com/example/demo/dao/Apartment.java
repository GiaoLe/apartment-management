package com.example.demo.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    @Id
    @NotNull
    private Integer id;

    @NotNull
    private Integer number;

    @NotNull
    private Double area;
    @NotNull
    private Integer floorNumber;
    private Integer roomCount;
    @NotNull
    private String type;
    @NotNull
    private String status;
    public Apartment(Integer id, Integer number, Double area, String type, String status) {
        this.number = number;
        this.area = area;
        this.id = id;
        this.status = status;
        this.type = type;
        this.roomCount = 0;
        floorNumber = Integer.valueOf(id.toString().substring(0, 1));
    }
}

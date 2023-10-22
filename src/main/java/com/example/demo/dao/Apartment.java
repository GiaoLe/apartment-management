package com.example.demo.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
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
    @GeneratedValue
    @NotNull
    private Integer id;

    @NotEmpty
    private String number;

    @NotNull
    private double area;

    @NotNull
    private int floor;

    @NotNull
    private int roomCount;
    @NotNull
    private String type;
    public Apartment(String number, double area) {
        this.number = number;
        this.area = area;
        floor = Integer.parseInt(number.substring(0, 1));
    }
    public void setRoomCount(Integer number){
        this.roomCount+=number;
    }
}

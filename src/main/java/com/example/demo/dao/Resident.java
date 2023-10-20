package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Resident {
    @Id
    @GeneratedValue
    private Integer id;

    private String firstName;

    private String lastName;
    public Resident(String firstName, String lastName, Apartment apartment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.apartment = apartment;
    }

    @ManyToOne
    private Apartment apartment;
}

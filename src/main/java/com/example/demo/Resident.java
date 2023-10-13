package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    private Apartment apartment;

}

package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    public Resident(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    @ManyToOne
//    private Apartment apartment;

}

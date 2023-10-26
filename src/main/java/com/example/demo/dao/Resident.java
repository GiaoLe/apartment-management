package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @NotNull
    private String phoneNumber;

    private String email;

    private String nationalID;
    @ManyToOne
    private Apartment apartment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resident", fetch = FetchType.EAGER)
    private List<ResidentCollection> residentCollectionList;
    public Resident(String firstName, String lastName, Apartment apartment, String phoneNumber, String email, String nationalID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalID = nationalID;
    }
}

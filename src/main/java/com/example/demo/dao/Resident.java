package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Resident implements Serializable {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
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

    public String getApartment() {
        return apartment.getId();
    }

    @SuppressWarnings("unused")
    public Integer getResidentCollectionList() {
        return residentCollectionList.size();
    }
}

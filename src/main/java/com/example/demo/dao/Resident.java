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
    @Setter(AccessLevel.NONE)
    @GeneratedValue
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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resident", fetch = FetchType.EAGER)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<ResidentCollection> residentCollectionList;

    public Resident( String firstName, String lastName, Apartment apartment, String phoneNumber, String email, String nationalID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalID = nationalID;
    }

    public String getApartmentID() {
        return apartment.getId();
    }
    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", nationalID='" + nationalID + '\'' +
                ", apartment=" + "id=" + apartment.getId() + // Avoid calling toString on the entire apartment
                '}';
    }

}

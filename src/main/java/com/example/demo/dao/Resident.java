package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resident implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    private Integer id;

    @NotNull
    private String firstName;
    @NotNull
    private Date timeMoveIn;
    @NotNull
    private String lastName;
    @NotNull
    private String IDNumber;

    @NotNull
    private String phoneNumber;

    private String email;

    private String nationalID;
    @ManyToOne
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;


    public Resident(String IDNumber, String firstName, String lastName, Apartment apartment, String phoneNumber, String email, String nationalID, Date date) {
        this.IDNumber = IDNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalID = nationalID;
        this.timeMoveIn = date;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", IDNumber='" + IDNumber + '\'' +
                ", timeMoveIn=" + timeMoveIn +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", nationalID='" + nationalID + '\'' +
                ", apartment=" + "id=" + apartment.getId() +
                '}';
    }

    public String getApartmentID() {
        return apartment.getId();
    }

}

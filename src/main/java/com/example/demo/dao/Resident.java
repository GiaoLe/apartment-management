package com.example.demo.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

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
    private Date moveInDate;
    @NotNull
    private String lastName;
    @NotNull
    private String IDNumber;
    @NotNull
    private Boolean gender;
    @NotNull
    private String phoneNumber;

    private String email;

    private String nationalID;

    @NotNull
    private Date dateOfBirth;


    @ManyToOne
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Apartment apartment;

    public Resident(String IDNumber, Date dateOfBirth, String gender, String firstName, String lastName, Apartment apartment, String phoneNumber, String email, String nationalID, Date date) {
        this.IDNumber = IDNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender.equals("Female");
        this.firstName = firstName;
        this.lastName = lastName;
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationalID = nationalID;
        this.moveInDate = date;
    }

    @Override
    public String toString() {
         return "Resident{" +
                "id='" + id + '\'' +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email +
                ", nationalID= " + nationalID +
                ", moveInDate=" + moveInDate +
                ", apartment=" + apartment.getId();
    }

    public String getApartmentID() {
        return apartment.getId();
    }


}

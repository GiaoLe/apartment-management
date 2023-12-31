package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentCollection {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Apartment apartment;

    @ManyToOne
    private Collection collection;

    @NotNull
    private Date deadlinePayment;

    @NotNull
    private boolean isPaid;

    @Override
    public String toString() {
        return "ResidentCollection{" +
                "id=" + id +
                ", collection=" + collection.getId() +
                ", apartment=" + apartment.getId() +
                ", deadlinePayment = " + deadlinePayment +
                ", isPaid=" + isPaid +
                '}';
    }

    public ApartmentCollection(Apartment apartment, Collection collection, Date deadlinePayment) {
        this.apartment = apartment;
        this.collection = collection;
        this.deadlinePayment = deadlinePayment;
        isPaid = false;
    }
}
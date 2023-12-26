package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentCollection {

    @EmbeddedId
    private ApartmentCollectionID id;

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
                "collection=" + collection.getId() +
                ", apartment=" + apartment.getId() +
                ", deadlinePayment = " + deadlinePayment +
                ", isPaid=" + isPaid +
                '}';
    }

    public ApartmentCollection(Apartment apartment, Collection collection, Date deadlinePayment) {
        id = new ApartmentCollectionID(Integer.parseInt(apartment.getId()), collection.getId());
        this.apartment = apartment;
        this.collection = collection;
        this.deadlinePayment = deadlinePayment;
        isPaid = false;
    }
}
package com.example.demo.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Override
    public String toString() {
        return "ResidentCollection{" +
                "collection=" + collection.getId() +
                ", isPaid=" + isPaid +
                '}';
    }
    private boolean isPaid;

    public ApartmentCollection(Apartment apartment, Collection collection) {
        id = new ApartmentCollectionID(Integer.parseInt(apartment.getId()), collection.getId());
        this.apartment = apartment;
        this.collection = collection;
        isPaid = false;
    }
}
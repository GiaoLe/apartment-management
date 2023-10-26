package com.example.demo.dao;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResidentCollection {

    @EmbeddedId
    private ResidentCollectionID id;

    @ManyToOne
    private Resident resident;

    @ManyToOne
    private Collection collection;

    private boolean isPaid;

    public ResidentCollection(Resident resident, Collection collection) {
        id = new ResidentCollectionID(resident.getId(), collection.getId());
        this.resident = resident;
        this.collection = collection;
        isPaid = false;
    }

}
package com.example.demo.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

//TODO Improve amount calculation (by implementing a formula in CollectionType)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;
    @NotNull
    private CollectionType type;
    @NotNull
    private Double amount;

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", timeToPay=" + timeToPay +
                ", description='" + description + '\'' +
                '}';
    }

    @NotNull
    private Date timeToPay;

    private String description;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "collection")
    private List<ApartmentCollection> apartmentCollections;
}
package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {
    @Id
    @GeneratedValue
    private Integer id;

    private CollectionType type;

    private double amount;

//    private Date startDate;
//
//    private Date endDate;
//
//    private String description;

    @ManyToMany(cascade = CascadeType.MERGE)
    @NotNull
    private Set<Resident> residents = new HashSet<>();

}

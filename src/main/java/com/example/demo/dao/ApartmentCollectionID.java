package com.example.demo.dao;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentCollectionID implements Serializable {
    private Integer apartmentID;
    private Integer collectionID;
}
package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.repository.HibernateUtility;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ApartmentFilter {
    private final TextField apartmentIDFilter;
    private final TextField hostNameFilter;
    private final MenuButton stateMenu;
    private final MenuButton typeMenu;
    private final Button submitFilter;
    private final List<MenuItem> typeItems;
    private final List<MenuItem> stateItems;
    private final  List<Apartment> apartments;

    public ApartmentFilter(TextField apartmentIDFilter, TextField hostNameFilter, MenuButton stateMenu, MenuButton typeMenu, Button submitFilter, List<MenuItem> typeItems, List<MenuItem> stateItems, List<Apartment> apartments) {
        this.apartmentIDFilter = apartmentIDFilter;
        this.hostNameFilter = hostNameFilter;
        this.stateMenu = stateMenu;
        this.typeMenu = typeMenu;
        this.submitFilter = submitFilter;
        this.typeItems = typeItems;
        this.stateItems = stateItems;
        this.apartments = apartments;
    }
    public List<Apartment> handleFilter() {
        List<Apartment> filteredApartment = new ArrayList<>();
        if(!apartmentIDFilter.getText().isEmpty()) {
            for (Apartment apartment : apartments) {
                if (Objects.equals(apartment.getId(), apartmentIDFilter.getText())) {
                    filteredApartment.add(apartment);
                }
            }
        } else if (apartmentIDFilter.getText().isEmpty()){
            if(hostNameFilter.getText().isEmpty()){
                if(Objects.equals(typeMenu.getText(), "Choose Type")){
                    if(Objects.equals(stateMenu.getText(), "Choose State")){
                        filteredApartment = apartments;
                    }else {
                        for(Apartment apartment : apartments){
                            if(Objects.equals(String.valueOf(apartment.getState()), stateMenu.getText())){
                                filteredApartment.add(apartment);
                            }
                        }
                    }
                } else {
                    if(Objects.equals(stateMenu.getText(), "Choose State")) {
                        for (Apartment apartment : apartments) {
                            if (Objects.equals(String.valueOf(apartment.getType()), typeMenu.getText())) {
                                filteredApartment.add(apartment);
                            }
                        }
                    }
                    else {
                        for (Apartment apartment1 : apartments){
                            if(Objects.equals(String.valueOf(apartment1.getState()), stateMenu.getText()) && Objects.equals(String.valueOf(apartment1.getType()), typeMenu.getText())){
                                filteredApartment.add(apartment1);
                            }
                        }
                    }

                }
            }
        } else {
            if(Objects.equals(typeMenu.getText(), "Choose Type")){
                if(Objects.equals(stateMenu.getText(), "Choose State")){

                }
            }
        }
        return filteredApartment;

    }
}

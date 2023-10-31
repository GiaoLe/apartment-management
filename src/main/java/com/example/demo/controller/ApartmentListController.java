package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApartmentListController {

    public TableView<ObservableMap<String, String>> apartmentTableView;
    public TableColumn<String, String> availableColumn;
    public Button deleteButton;
    public TableColumn<ObservableMap<String, String>, String> floorColumn;
    public TableColumn<ObservableMap<String, String>, String> nAvailableColumn;
    public Button newButton;
    public TableColumn<ObservableMap<String, String>, String> occupiedColumn;
    public TableColumn<ObservableMap<String, String>, String> residentsColumn;
    public TableColumn<ObservableMap<String, String>, String> totalColumn;
    private final Map<String, String> floorDetails = new HashMap<>();
    private ObservableList<ObservableMap<String, String>> floorList;
    private final ObservableMap<String, String> firstFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> secondFloor = FXCollections.observableHashMap();

    public void updateFloorDetails() {
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment ", Apartment.class)
                .getResultList());
         floorList = FXCollections.observableArrayList();
        for(Apartment apartment : apartments){
            int numberOfApartment = 0;

            switch (apartment.getFloor()){
                case 1:
                    numberOfApartment++;
                    firstFloor.put("floor", "1");
                    firstFloor.put("totalApartments", String.valueOf(numberOfApartment));
                case 2:
                    numberOfApartment++;
                    secondFloor.put("floor", "2");
                    secondFloor.put("totalApartments", String.valueOf(numberOfApartment));
            }
        }
        floorList.add(firstFloor);
        floorList.add(secondFloor);
    }
    public void showFloorList() {
        updateFloorDetails();
        floorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("floor")));
        totalColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalApartments")));
        apartmentTableView.setItems(floorList);
    }
    public void initialize() {
        showFloorList();
    }

    public void newButtonOnAction() {
        MenuViewManager.switchView(MenuView.APARTMENT_FORM);
    }

    public void deleteButtonOnAction() {
    }
}

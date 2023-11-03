package com.example.demo.controller;

import com.example.demo.dao.Collection;
import com.example.demo.dao.ResidentCollection;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//TODO Change Collection Report to each Apartment, not to each Resident
public class CollectionReportController {

    public TableView<ResidentCollection> collectionReportTableView;
    public TableColumn<ResidentCollection, Integer> residentIDTableColumn;
    public TableColumn<ResidentCollection, String> firstNameTableColumn;
    public TableColumn<ResidentCollection, String> lastNameTableColumn;
    public TableColumn<ResidentCollection, String> apartmentTableColumn;
    public TableColumn<ResidentCollection, Double> amountTableColumn;
    public TableColumn<ResidentCollection, Boolean> isPaidTableColumn;
    public TextField collectionName;

    public void initializeData(Collection collection) {
        collectionName.setText(collection.getName());

        collectionReportTableView.setItems(FXCollections.observableList(collection.getResidentCollections()));
        residentIDTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResident().getId()));
        firstNameTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResident().getFirstName()));
        lastNameTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResident().getLastName()));
        apartmentTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResident().getApartmentID()));
        amountTableColumn.setCellValueFactory(cellData -> {
            ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
            double apartmentArea = apartmentService.findByID(cellData.getValue().getResident().getApartmentID()).getArea();
            Double amount =
                    switch (collection.getType()) {
                        case SERVICE_FEE, MANAGEMENT_FEE -> collection.getAmount() * apartmentArea;
                        default -> collection.getAmount();
                    };
            return new SimpleObjectProperty<>(amount);
        });
        isPaidTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isPaid()));
    }

    public void backButtonOnAction() {
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }
}

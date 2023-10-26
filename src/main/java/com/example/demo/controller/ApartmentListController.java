package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ApartmentListController {

    public TableView<Apartment> apartmentTableView;
    public TableColumn<Apartment, String> numberTableColumn;
    public TableColumn<Apartment, Double> areaTableColumn;
    public TableColumn<Apartment, String> totalRoomsTableColumn;
    public TableColumn<Apartment, Integer> floorTableColumn;
    public TableColumn<Apartment, Integer> totalResidentsTableColumn;
    public Button newButton;
    public Button deleteButton;

    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());


    @FXML
    public void initialize() {
        apartmentTableView.setItems(FXCollections.observableList(apartmentService.findAll()));
        numberTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumber()));
        areaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        totalRoomsTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRoomCount()).asString());
        floorTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFloor()));
        totalResidentsTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalResidents()));
    }

    public void newButtonOnAction() {
        MenuViewManager.switchView(MenuView.APARTMENT_FORM);
    }

    public void deleteButtonOnAction() {
        Apartment apartment = apartmentTableView.getSelectionModel().getSelectedItem();
        if (apartment != null) {
            apartmentTableView.getItems().remove(apartment);
            apartmentService.remove(apartment);
        }
    }
}

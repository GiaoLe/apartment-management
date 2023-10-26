package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ApartmentListController {

    public Button newButton;
    public TableView<Apartment> apartmentTableView;
    public TableColumn<Apartment, String> numberTableColumn;
    public TableColumn<Apartment, Double> areaTableColumn;
    public TableColumn<Apartment, String> totalRoomsTableColumn;
    public TableColumn<Apartment, Integer> floorTableColumn;


    @FXML
    public void initialize() {
        ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
        apartmentTableView.setItems(FXCollections.observableList(apartmentService.findAll()));
        numberTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));
        areaTableColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()).asObject());
        totalRoomsTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRoomCount()).asString());
        floorTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFloor()).asObject());
    }

    public void newButtonOnAction() {
        SceneManager.switchScene(Scene.APARTMENT_FORM.getFileName());
    }
}

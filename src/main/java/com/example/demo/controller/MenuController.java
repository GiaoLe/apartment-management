package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MenuController {

    public Button apartmentButton;

    public Button dashboardButton;
    public TableColumn<Apartment, Integer> availableRooms;
    public TableColumn<?, ?> description;
    public TableView<?> flatList;
    public TableColumn<?, ?> flatNumber;
    public TableColumn<?, ?> notAvailableRooms;
    public TableColumn<?, ?> occupiedRooms;
    public TableColumn<?, ?> totalResidents;
    public TableColumn<?, ?> totalRooms;

    @FXML
    public void initialize() {
        dashboardButton.setOnAction(actionEvent -> dashboardButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true"));
        apartmentButton.setOnAction(actionEvent -> apartmentButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true"));
    }
}

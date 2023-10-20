package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import javafx.event.ActionEvent;
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

    public void clickNavigation(ActionEvent actionEvent) {
        String buttonID = ((Button) actionEvent.getSource()).getId();
        if (buttonID.contains("dashboardButton")) {
            dashboardButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            apartmentButton.setStyle("-fx-text-fill: #979191;" + "-fx-underline: false");
        } else if (buttonID.contains("apartmentButton")) {
            apartmentButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            dashboardButton.setStyle("-fx-text-fill: #979191;" + "-fx-underline: false");
        }
    }
}

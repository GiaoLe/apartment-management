package com.example.demo.controller;

import com.example.demo.gui.SceneManager;
import com.example.demo.gui.Scene;
import com.example.demo.dao.Apartment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;

public class MenuController {

    public Button dashboardButton;
    public TableColumn<Apartment, Integer> availableRooms;
    public TableColumn<?, ?> description;
    public TableColumn<?, ?> flatNumber;
    public TableColumn<?, ?> notAvailableRooms;
    public TableColumn<?, ?> occupiedRooms;
    public TableColumn<?, ?> totalResidents;
    public TableColumn<?, ?> totalRooms;
    public Button residentsButton;
    public BorderPane borderPane;
    public Button apartmentsButton;

    @FXML
    public void initialize() {
        SceneManager.setBorderPane(borderPane);
        dashboardButtonOnAction();
    }

    public void residentsButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }

    public void apartmentsButtonOnAction() {
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }

    public void dashboardButtonOnAction() {
        SceneManager.switchScene(Scene.DASHBOARD.getFileName());
    }
}

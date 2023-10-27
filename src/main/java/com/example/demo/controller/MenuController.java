package com.example.demo.controller;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
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
    public Button collectionsButton;

    @FXML
    public void initialize() {
        MenuViewManager.setBorderPane(borderPane);
        dashboardButtonOnAction();
    }

    public void residentsButtonOnAction() {
        MenuViewManager.switchView(MenuView.RESIDENT_LIST);
    }

    public void apartmentsButtonOnAction() {
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }

    public void dashboardButtonOnAction() {
        MenuViewManager.switchView(MenuView.DASHBOARD);
    }

    public void collectionsButtonOnAction() {
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }
}

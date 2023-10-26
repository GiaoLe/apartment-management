package com.example.demo.controller;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MenuController {

    public BorderPane borderPane;
    public Button dashboardButton;
    public Button residentsButton;
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

package com.example.demo.controller;

import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
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

    public void collectionsButtonOnAction() {
        SceneManager.switchScene(Scene.COLLECTION_LIST.getFileName());
    }
}

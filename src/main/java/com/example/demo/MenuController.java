package com.example.demo;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class MenuController {

    public Button apartmentListButton;
    public Button residentListButton;
    public MenuItem createCollectionMenuItem;
    public MenuItem reportsMenuItem;

    public void residentListButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }

    public void apartmentListButtonOnAction() {
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }

    public void reportsMenuItemOnAction() {
        SceneManager.switchScene(Scene.REPORT_LIST.getFileName());
    }

    public void createCollectionMenuItemOnAction() {
        SceneManager.switchScene(Scene.COLLECTION_FORM.getFileName());
    }
}

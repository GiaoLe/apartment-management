package com.example.demo.controller;

import com.example.demo.Scene;
import com.example.demo.SceneManager;
import javafx.scene.control.Button;

public class MenuController {

    public Button apartmentListButton;
    public Button residentListButton;
    public Button collectionButton;

    public void residentListButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }

    public void apartmentListButtonOnAction() {
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }

    public void collectionButtonOnAction() {
        SceneManager.switchScene(Scene.COLLECTION_LIST.getFileName());
    }
}

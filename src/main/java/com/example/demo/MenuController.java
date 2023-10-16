package com.example.demo;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MenuController {

    public Button apartmentListButton;
    public Button residentListButton;

    public void residentListButtonOnAction(ActionEvent actionEvent) {
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }

    public void apartmentListButtonOnAction(ActionEvent actionEvent) {
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }
}

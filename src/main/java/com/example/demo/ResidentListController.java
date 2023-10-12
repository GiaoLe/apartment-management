package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ResidentListController {

    public ListView<Resident> residentListView;
    public Button newResidentButton;

    private ResidentService residentService;

    @FXML
    public void initialize() {
        residentService = new ResidentService(new ResidentRepository());
        residentListView.getItems().addAll(residentService.findAll());
    }

    public void newResidentButtonOnAction(ActionEvent actionEvent) {
        SceneManager.switchScene(Scene.RESIDENT_FORM.getFileName());
    }
}

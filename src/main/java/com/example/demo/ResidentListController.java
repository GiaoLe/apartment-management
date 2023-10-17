package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

public class ResidentListController {

    public ListView<String> residentListView;
    public Button newResidentButton;
    public Button menuButton;

    @FXML
    public void initialize() {
        ResidentService residentService = new ResidentService(new ResidentRepository());
        residentListView.getItems().addAll(residentService.findAll()
                .stream()
                .map(resident ->
                        "ID: " + resident.getId() + "\n" +
                        "First name: " + resident.getFirstName() + "\n" +
                        "Last name: " + resident.getLastName() + "\n" +
                        "Apartment number: " + resident.getApartment().getNumber() + "\n")
                .collect(Collectors.toList()));
    }

    public void newResidentButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_FORM.getFileName());
    }

    public void menuButtonOnAction() {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }
}

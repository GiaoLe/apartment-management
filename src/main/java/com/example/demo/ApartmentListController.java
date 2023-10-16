package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ApartmentListController {

    public ListView<Apartment> apartmentListView;
    public Button menuButton;
    public Button newButton;

    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        apartmentService = new ApartmentService(new ApartmentRepository());
        apartmentListView.getItems().addAll(apartmentService.getAll());
    }

    public void menuButtonOnAction(ActionEvent actionEvent) {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }

    public void newButtonOnAction(ActionEvent actionEvent) {
        SceneManager.switchScene(Scene.APARTMENT_FORM.getFileName());
    }
}

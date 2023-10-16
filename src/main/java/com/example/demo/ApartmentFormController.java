package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ApartmentFormController {
    public TextField nameTextField;
    public TextField areaTextField;

    public Button submitButton;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        Apartment apartment = new Apartment(nameTextField.getText(),
                Double.parseDouble(areaTextField.getText()));
        apartmentService.persist(apartment);
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }
}

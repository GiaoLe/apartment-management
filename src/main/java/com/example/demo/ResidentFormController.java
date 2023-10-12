package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ResidentFormController {

    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField apartmentTextField;
    public Button submitButton;

    private ResidentService residentService;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        residentService = new ResidentService(new ResidentRepository());
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        Apartment apartment = new Apartment(apartmentTextField.getText());
        apartmentService.merge(apartment);
        residentService.persist(new Resident(
                firstNameTextField.getText(),
                lastNameTextField.getText(),
                apartment
        ));
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }
}

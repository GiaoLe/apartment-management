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

    @FXML
    public void initialize() {
        residentService = new ResidentService(new ResidentRepository());
    }

    public void submitButtonOnAction() {
        residentService.save(new Resident(
                firstNameTextField.getText(),
                lastNameTextField.getText()
        ));
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }
}

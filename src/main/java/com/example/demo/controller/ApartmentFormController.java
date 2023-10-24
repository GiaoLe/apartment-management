package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.dao.Apartment;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ApartmentFormController {

    public TextField areaTextField;


    public TextField idTextField;


    public TextField numberTextField;


    public TextField typeTextField;
    public TextField statusTextField;
    public Button submitButton;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        Apartment apartment = new Apartment(Integer.parseInt(idTextField.getText()),
                Integer.parseInt(numberTextField.getText()),
                Double.parseDouble(areaTextField.getText()),
                typeTextField.getText(),
                statusTextField.getText());
        apartmentService.persist(apartment);
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }
}

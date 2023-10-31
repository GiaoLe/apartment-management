package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ApartmentFormController {
    public TextField idTextField;
    public TextField areaTextField;
    public Button submitButton;
    public TextField roomCountTextField;
    public TextField statusTextField;
    public TextField typeTextField;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        Apartment apartment = Apartment.builder()
                .id(idTextField.getText())
                .area(Double.parseDouble(areaTextField.getText()))
                .roomCount(Integer.parseInt(roomCountTextField.getText()))
                .status(statusTextField.getText())
                .type(typeTextField.getText())
                .build();
        apartmentService.persist(apartment);
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }
}

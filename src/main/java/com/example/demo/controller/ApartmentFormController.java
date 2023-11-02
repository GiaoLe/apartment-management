package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentState;
import com.example.demo.dao.ApartmentType;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ApartmentFormController {
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    public TextField idTextField;
    public TextField areaTextField;
    public Button submitButton;
    public TextField roomCountTextField;
    public ChoiceBox<ApartmentType> apartmentTypeChoiceBox;

    @FXML
    public void initialize() {
        apartmentTypeChoiceBox.getItems().addAll(ApartmentType.values());
    }

    public void submitButtonOnAction() {
        Apartment apartment = Apartment.builder()
                .id(idTextField.getText())
                .area(Double.parseDouble(areaTextField.getText()))
                .roomCount(Integer.parseInt(roomCountTextField.getText()))
                .type(apartmentTypeChoiceBox.getValue())
                .state(ApartmentState.AVAILABLE)
                .build();
        apartmentService.persist(apartment);
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }
}

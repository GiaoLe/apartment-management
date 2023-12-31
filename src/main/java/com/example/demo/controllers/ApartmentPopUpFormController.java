package com.example.demo.controllers;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentState;
import com.example.demo.dao.ApartmentType;
import com.example.demo.gui.PopUpWindowManager;
import com.example.demo.services.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.controlsfx.control.PrefixSelectionChoiceBox;

public class ApartmentPopUpFormController {


    private final ApartmentService apartmentService = new ApartmentService();
    public TextField idTextField;
    public TextField areaTextField;
    public TextField roomCountTextField;
    public PrefixSelectionChoiceBox<ApartmentType> apartmentTypeChoiceBox;

    @FXML
    public void initialize() {
        apartmentTypeChoiceBox.getItems().addAll(ApartmentType.values());
    }

    public void submitButtonOnAction() {
        persistApartment();
    }

    private void persistApartment() {
        apartmentService.persist(new Apartment(
                idTextField.getText(),
                Double.parseDouble(areaTextField.getText()),
                apartmentTypeChoiceBox.getValue(),
                ApartmentState.AVAILABLE,
                Integer.parseInt(roomCountTextField.getText()),
                null
        ));
        PopUpWindowManager.closeCurrentWindow();
    }
}

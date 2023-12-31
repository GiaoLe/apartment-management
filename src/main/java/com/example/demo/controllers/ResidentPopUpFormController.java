package com.example.demo.controllers;

import com.example.demo.dao.*;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.gui.PopUpWindowManager;
import com.example.demo.repositories.ApartmentRepository;
import com.example.demo.services.ApartmentService;
import com.example.demo.services.ResidentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ResidentPopUpFormController {

    public Button submitButton;
    public TextField firstNameTextField;
    public Label firstNameErrorLabel;
    public TextField lastNameTextField;
    public Label lastNameErrorLabel;
    public DatePicker dateOfBirthDatePicker;
    public TextField phoneNumberTextField;
    public Label phoneNumberErrorLabel;
    public ChoiceBox<Gender> genderChoiceBox;
    public TextField apartmentTextField;
    public Label apartmentErrorLabel;
    public TextField nationalIDTextField;
    public Label nationalIDErrorLabel;
    public DatePicker datePicker;
    public TextField emailTextField;
    public Label emailErrorLabel;

    private List<TextFieldWithErrorLabelWrapper> textFieldWithErrorLabelWrappers;

    private final ApartmentService apartmentService = new ApartmentService();

    private final ResidentService residentService = new ResidentService();

    @FXML
    public void initialize() {
        initializeGenderChoiceBox();

        textFieldWithErrorLabelWrappers = new ArrayList<>(
                List.of(new TextFieldWithErrorLabelWrapper(firstNameTextField, firstNameErrorLabel),
                        new TextFieldWithErrorLabelWrapper(lastNameTextField, lastNameErrorLabel),
                        new TextFieldWithErrorLabelWrapper(apartmentTextField, apartmentErrorLabel),
                        new TextFieldWithErrorLabelWrapper(nationalIDTextField, nationalIDErrorLabel),
                        new TextFieldWithErrorLabelWrapper(phoneNumberTextField, phoneNumberErrorLabel),
                        new TextFieldWithErrorLabelWrapper(emailTextField, emailErrorLabel)));
    }

    private void initializeGenderChoiceBox() {
        genderChoiceBox.getItems().addAll(Gender.values());
    }

    public void submitButtonOnAction() {
        for (TextFieldWithErrorLabelWrapper textFieldWithErrorLabelWrapper : textFieldWithErrorLabelWrappers) {
            textFieldWithErrorLabelWrapper.checkEmpty();
        }

        persistResident();

        PopUpWindowManager.closeCurrentWindow();
    }

    private void persistResident() {
        Apartment apartment = apartmentService.findByID(apartmentTextField.getText());
        if (apartment == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(
                    "Apartment with id " + apartmentTextField.getText() + " does not exist. Please create it first.");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(
                    response -> MenuViewManager.switchView(MenuView.APARTMENT_FORM));
        } else {
            Resident resident = new Resident(firstNameTextField.getText(), lastNameTextField.getText(),
                                             genderChoiceBox.getValue(), Date.valueOf(dateOfBirthDatePicker.getValue()),
                                             nationalIDTextField.getText(), phoneNumberTextField.getText(),
                                             emailTextField.getText(), apartment, Date.valueOf(datePicker.getValue()));

            residentService.persist(resident);
            apartment.addResident(resident);
            apartment.setState(ApartmentState.OCCUPIED);
            ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
            apartmentService.merge(apartment);

        }
    }
}

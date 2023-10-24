package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.TextFieldWrapper;
import com.example.demo.dao.Apartment;
import com.example.demo.dao.Resident;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class ResidentFormController {

    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField apartmentTextField;
    public Button submitButton;
    public Label apartmentTextFieldErrorLabel;
    public Label firstNameErrorLabel;
    public Label lastNameErrorLabel;
    public Button backButton;
    public TextField nationalIDTextField;
    public Label nationalIDErrorLabel;
    public TextField phoneNumberTextField;
    public Label phoneNumberErrorLabel;
    public TextField emailTextField;
    public Label emailErrorLabel;

    private ArrayList<TextFieldWrapper> textFieldWrappers;

    private ResidentService residentService;

    @FXML
    public void initialize() {
        textFieldWrappers = new ArrayList<>(List.of(
                new TextFieldWrapper(firstNameTextField, firstNameErrorLabel),
                new TextFieldWrapper(lastNameTextField, lastNameErrorLabel),
                new TextFieldWrapper(apartmentTextField, apartmentTextFieldErrorLabel),
                new TextFieldWrapper(nationalIDTextField, nationalIDErrorLabel),
                new TextFieldWrapper(phoneNumberTextField, phoneNumberErrorLabel),
                new TextFieldWrapper(emailTextField, emailErrorLabel)
        ));
        residentService = new ResidentService(new ResidentRepository());
    }

    public void submitButtonOnAction() {
        for (TextFieldWrapper textFieldWrapper : textFieldWrappers) {
            textFieldWrapper.checkEmpty();
        }
        boolean allFieldsAreFilled = textFieldWrappers.stream().noneMatch(TextFieldWrapper::isEmpty);
        if (allFieldsAreFilled) {
            persistResident();
        }
    }

    private void persistResident() {
        Apartment apartment = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment where number = :number", Apartment.class)
                .setParameter("number", apartmentTextField.getText())
                .uniqueResult());
        if (apartment == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Apartment with number " + apartmentTextField.getText() + " does not exist. Please create it first.");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> SceneManager.switchScene(Scene.APARTMENT_FORM.getFileName()));
        } else {
            residentService.persist(new Resident(
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    apartment,
                    nationalIDTextField.getText(),
                    phoneNumberTextField.getText(),
                    emailTextField.getText()));
            SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
        }
    }

    public void backButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
    }
}


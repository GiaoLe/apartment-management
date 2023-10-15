package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;

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

    private ArrayList<TextFieldWrapper> textFieldWrappers;

    private ResidentService residentService;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        textFieldWrappers = new ArrayList<>(List.of(
                new TextFieldWrapper(firstNameTextField, firstNameErrorLabel),
                new TextFieldWrapper(lastNameTextField, lastNameErrorLabel),
                new TextFieldWrapper(apartmentTextField, apartmentTextFieldErrorLabel)));
        residentService = new ResidentService(new ResidentRepository());
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        for (TextFieldWrapper textFieldWrapper : textFieldWrappers) {
            textFieldWrapper.checkEmpty();
        }
        boolean allFieldsAreFilled = textFieldWrappers.stream().noneMatch(TextFieldWrapper::isEmpty);
        if (allFieldsAreFilled) {
            persistResident();
            SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
        }
    }

    private void persistResident() {
        Apartment savedApartment;
        Integer apartmentNumber = Integer.parseInt(apartmentTextField.getText());
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            savedApartment = session.createQuery("from Apartment where number = :number", Apartment.class)
                    .setParameter("number", apartmentNumber)
                    .uniqueResult();
        }
        if (savedApartment == null) {
            savedApartment = new Apartment(apartmentNumber);
            apartmentService.persist(savedApartment);
        }
        residentService.persist(new Resident(
                firstNameTextField.getText(),
                lastNameTextField.getText(),
                savedApartment));
    }
}


package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;

public class ResidentFormController {

    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField apartmentTextField;
    public Button submitButton;
    public Label apartmentTextFieldErrorLabel;

    private ResidentService residentService;
    private ApartmentService apartmentService;

    @FXML
    public void initialize() {
        apartmentTextFieldErrorLabel.setText("");
        residentService = new ResidentService(new ResidentRepository());
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        if (!apartmentTextField.getText().isEmpty()) {
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
                    savedApartment)
            );
            SceneManager.switchScene(Scene.RESIDENT_LIST.getFileName());
        } else {
            apartmentTextFieldErrorLabel.setText("Apartment number is required");
        }
    }
}

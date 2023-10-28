package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ApartmentFormController {
    public TextField nameTextField;
    public TextField areaTextField;
    public Button submitButton;
    public TextField roomCountTextField;
    public Button backButton;
    public TextField idTextField;
    public TextField numberTextField;
    private ApartmentService apartmentService;
    public TextField statusTextField;
    public TextField typeTextField;
    @FXML
    public void initialize() {
        apartmentService = new ApartmentService(new ApartmentRepository());
    }

    public void submitButtonOnAction() {
        Apartment apartment = new Apartment(nameTextField.getText(),
                Double.parseDouble(areaTextField.getText()),
                Integer.parseInt(roomCountTextField.getText()));
        apartmentService.persist(apartment);
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }

    public void countRoom() {
        Object count = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("select count (*) from Apartment ", Apartment.class)
                .getResultList());
        System.out.println(count);
    }
}

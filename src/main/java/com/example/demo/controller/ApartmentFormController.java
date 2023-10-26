package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.dao.Apartment;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
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
//        Apartment apartment = new Apartment(Integer.parseInt(idTextField.getText()),
//                numberTextField.getText(),
//                Double.parseDouble(areaTextField.getText()),
//                typeTextField.getText(),
//                statusTextField.getText()
//                );
//        apartmentService.persist(apartment);
        countRoom();
        SceneManager.switchScene(Scene.APARTMENT_LIST.getFileName());
    }
    public void handleBackAction() {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }
    public void countRoom() {
        Object count = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("select count (*) from Apartment ", Apartment.class)
                .getResultList());
        System.out.println(count);
    }
}

package com.example.demo.controller;

import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

public class ApartmentListController {

    public ListView<String> apartmentListView;
    public Button menuButton;
    public Button newButton;

    @FXML
    public void initialize() {
        ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
        apartmentListView.getItems().addAll(
                apartmentService.findAll()
                        .stream()
                        .map(apartment -> "ID: " + apartment.getId() + "\n"
                                + "Number: " + apartment.getNumber() + "\n"
                                + "Area: " + apartment.getArea() + "\n"
                                + "Floor: " + apartment.getFloor() + "\n")
                        .collect(Collectors.toList()));
    }

    public void menuButtonOnAction() {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }

    public void newButtonOnAction() {
        SceneManager.switchScene(Scene.APARTMENT_FORM.getFileName());
    }
}

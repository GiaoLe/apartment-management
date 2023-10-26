package com.example.demo.controller;

import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.dao.Resident;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.ResidentCollectionRepository;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.CollectionService;
import com.example.demo.service.ResidentCollectionService;
import com.example.demo.service.ResidentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CollectionFormController {

    public TextField amountTextField;
    public ChoiceBox<CollectionType> collectionTypeChoiceBox;
    public Button submitButton;
    public TextField descriptionTextField;
    public TextField nameTextField;

    @FXML
    public void initialize() {
        collectionTypeChoiceBox.getItems().addAll(CollectionType.values());
    }

    public void submitButtonOnAction() {
        List<Resident> residents = new ResidentService(new ResidentRepository()).findAll();
        if (residents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("There are no residents. Please create them first.");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> SceneManager.switchScene(Scene.RESIDENT_FORM.getFileName()));
        } else {
            Collection collection = Collection.builder()
                    .name(nameTextField.getText())
                    .amount(Double.parseDouble(amountTextField.getText()))
                    .type(collectionTypeChoiceBox.getValue())
                    .description(descriptionTextField.getText())
                    .build();
            CollectionService collectionService = new CollectionService(new CollectionRepository());
            collectionService.persist(collection);
            ResidentCollectionService residentCollectionService = new ResidentCollectionService(new ResidentCollectionRepository());
            for (Resident resident : residents) {
                residentCollectionService.persist(resident, collection);
            }
            SceneManager.switchScene(Scene.COLLECTION_LIST.getFileName());
        }
    }

}
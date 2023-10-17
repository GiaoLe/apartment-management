package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashSet;
import java.util.Set;

public class CollectionFormController {

    public TextField amountTextField;
    public ChoiceBox<CollectionType> collectionTypeChoiceBox;
    public Button submitButton;
    public Button backButton;

    @FXML
    public void initialize() {
        collectionTypeChoiceBox.getItems().addAll(CollectionType.values());
    }

    public void submitButtonOnAction() {
        Set<Resident> residents = new HashSet<>(new ResidentService(new ResidentRepository()).findAll());
        if (residents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("There are no residents. Please create them first.");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> SceneManager.switchScene(Scene.RESIDENT_FORM.getFileName()));
        } else {
            Collection collection = Collection.builder()
                    .amount(Double.parseDouble(amountTextField.getText()))
                    .type(collectionTypeChoiceBox.getValue())
                    .residents(residents)
                    .build();
            CollectionService collectionService = new CollectionService(new CollectionRepository());
            collectionService.persist(collection);
            SceneManager.switchScene(Scene.COLLECTION_LIST.getFileName());
        }
    }

    public void backButtonOnAction() {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }
}

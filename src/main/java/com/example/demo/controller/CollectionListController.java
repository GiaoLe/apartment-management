package com.example.demo.controller;

import com.example.demo.repository.CollectionRepository;
import com.example.demo.service.CollectionService;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

public class CollectionListController {
    public ListView<String> collectionListView;
    public Button menuButton;
    public Button newButton;

    @FXML
    public void initialize() {
        CollectionService collectionService = new CollectionService(new CollectionRepository());
        collectionListView.getItems().addAll(collectionService.findAll()
                .stream()
                .map(collection ->
                        "ID: " + collection.getId() + "\n" +
                        "Amount: " + collection.getAmount() + "\n")
                .collect(Collectors.toList()));
    }

    public void menuButtonOnAction() {
        SceneManager.switchScene(Scene.MENU.getFileName());
    }

    public void newButtonOnAction() {
        SceneManager.switchScene(Scene.COLLECTION_FORM.getFileName());
    }
}

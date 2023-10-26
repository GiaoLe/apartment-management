package com.example.demo.controller;

import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.service.CollectionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


//TODO Implement action listener for each row to open collection-resident list
public class CollectionListController {
    public Button newButton;
    public TableView<Collection> collectionsTableView;
    public TableColumn<Collection, String> nameTableColumn;
    public TableColumn<Collection, CollectionType> typeTableColumn;
    public TableColumn<Collection, Double> amountTableColumn;
    public TableColumn<Collection, String> descriptionTableColumn;

    @FXML
    public void initialize() {
        CollectionService collectionService = new CollectionService(new CollectionRepository());
        collectionsTableView.setItems(FXCollections.observableList(collectionService.findAll()));
        nameTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        typeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        amountTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        descriptionTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
    }

    public void newButtonOnAction() {
        SceneManager.switchScene(Scene.COLLECTION_FORM.getFileName());
    }
}

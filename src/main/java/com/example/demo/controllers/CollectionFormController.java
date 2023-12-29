package com.example.demo.controllers;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentCollection;
import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repositories.ApartmentCollectionRepository;
import com.example.demo.repositories.ApartmentRepository;
import com.example.demo.repositories.CollectionRepository;
import com.example.demo.repositories.HibernateUtility;
import com.example.demo.services.ApartmentCollectionService;
import com.example.demo.services.ApartmentService;
import com.example.demo.services.CollectionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class CollectionFormController {

    public TextField amountTextField;
    public ChoiceBox<CollectionType> collectionTypeChoiceBox;
    public Button submitButton;
    public TextArea descriptionTextArea;
    public TextField nameTextField;
    public TableColumn<Collection, Double> amountCol;
    public TableView<Collection> collectionTableView;
    public TableColumn<Collection, String> nameCol;
    public TableColumn<Collection, CollectionType> typeCol;
    public TableColumn<Collection, String> descriptionCol;

    @FXML
    public void initialize() {
        collectionTypeChoiceBox.getItems().addAll(CollectionType.values());
        updateCollectionTableView();
    }

    public void updateCollectionTableView() {
        List<Collection> collectionList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection order by name", Collection.class)
                .getResultList());
        collectionTableView.setItems(FXCollections.observableList(collectionList));
        nameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        amountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
    }

    public void submitButtonOnAction() {
        List<Apartment> apartments = new ApartmentService(new ApartmentRepository()).findAll();
        Collection collection = Collection.builder()
                .name(nameTextField.getText())
                .type(collectionTypeChoiceBox.getValue())
                .description(descriptionTextArea.getText())
                .amount(Double.parseDouble(amountTextField.getText()))
                .build();

        CollectionService collectionService = new CollectionService(new CollectionRepository());
        collectionService.persist(collection);

        ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
        if (collectionTypeChoiceBox.getValue() != CollectionType.DONATION){
            for (Apartment apartment : apartments) {
                Date deadlinePayment = Date.valueOf(LocalDate.now());
                deadlinePayment = Date.valueOf(deadlinePayment.toLocalDate().plusDays(30));
                apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, deadlinePayment));
            }
        }
        updateCollectionTableView();
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }
}
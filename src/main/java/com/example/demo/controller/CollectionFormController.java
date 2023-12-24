package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.*;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.List;

public class CollectionFormController {

    public TextField amountTextField;
    public ChoiceBox<CollectionType> collectionTypeChoiceBox;
    public Button submitButton;
    public TextField descriptionTextField;
    public TextField nameTextField;
    public DatePicker deadlineDate;
    public TableColumn<Collection, Double> amountCol;
    public TableView<Collection> collectionTableView;
    public TableColumn<Collection, Date> deadlineCol;
    public TableColumn<Collection, String> nameCol;
    public TableColumn<Collection, CollectionType> typeCol;

    @FXML
    public void initialize() {
        collectionTypeChoiceBox.getItems().addAll(CollectionType.values());
        updateCollectionTableView();
    }
    public void updateCollectionTableView(){
        List<Collection> collectionList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection order by name", Collection.class)
                .getResultList());
        collectionTableView.setItems(FXCollections.observableList(collectionList));
        nameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        amountCol.setCellValueFactory(celldata -> new SimpleObjectProperty<>(celldata.getValue().getAmount()));
        deadlineCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTimeToPay()));
    }

    public void submitButtonOnAction() {
        List<Apartment> apartments = new ApartmentService(new ApartmentRepository()).findAll();
        if (apartments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("There are no residents. Please create them first.");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> MenuViewManager.switchView(MenuView.RESIDENT_FORM));
        } else {
            Collection collection = Collection.builder().name(nameTextField.getText()).type(collectionTypeChoiceBox.getValue()).description(descriptionTextField.getText()).timeToPay(Date.valueOf(deadlineDate.getValue())).build();
            if (!amountTextField.getText().isEmpty()) {
                collection.setAmount(Double.parseDouble(amountTextField.getText()));
            }
            CollectionService collectionService = new CollectionService(new CollectionRepository());
            collectionService.persist(collection);
            ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());

            if (!collectionTypeChoiceBox.getValue().equals(CollectionType.DONATION)){
                for (Apartment apartment : apartments) {
                    apartmentCollectionService.persist(apartment, collection);
                }
            }
            updateCollectionTableView();
            MenuViewManager.switchView(MenuView.COLLECTION_LIST);
        }
    }

}
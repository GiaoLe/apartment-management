package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentCollectionRepository;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.HibernateUtility;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
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
    public void updateCollectionTableView(){
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
        if (apartments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("There is no resident. Please create them first.");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> MenuViewManager.switchView(MenuView.RESIDENT_FORM));
        } else {
            Collection collection = Collection.builder().name(nameTextField.getText()).type(collectionTypeChoiceBox.getValue()).description(descriptionTextArea.getText()).build();
            if (!amountTextField.getText().isEmpty()) {
                collection.setAmount(Double.parseDouble(amountTextField.getText()));
            }
            CollectionService collectionService = new CollectionService(new CollectionRepository());
            collectionService.persist(collection);
            ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());

            if (!collectionTypeChoiceBox.getValue().equals(CollectionType.DONATION)){
                for (Apartment apartment : apartments) {
                    if(!apartment.getResidents().isEmpty() && apartment.getHostname() != null){
                        LocalDate residentMoveInDate = apartment.getHostname().getMoveInDate().toLocalDate();
                        System.out.println(residentMoveInDate.plusDays(30));
                        residentMoveInDate = residentMoveInDate.plusMonths(1);
                        System.out.println(residentMoveInDate);
                        apartmentCollectionService.persist(apartment, collection, Date.valueOf(residentMoveInDate));
                    }
                }
            }
            updateCollectionTableView();
            MenuViewManager.switchView(MenuView.COLLECTION_LIST);
        }
    }

}
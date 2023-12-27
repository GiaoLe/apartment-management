package com.example.demo.controller;

import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.service.CollectionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CollectionListController {
    private final CollectionService collectionService = new CollectionService(new CollectionRepository());
    public Button newButton;
    public TableView<Collection> collectionsTableView;
    public TableColumn<Collection, String> nameTableColumn;
    public TableColumn<Collection, CollectionType> typeTableColumn;
    public TableColumn<Collection, String> amountTableColumn;
    public TableColumn<Collection, String> descriptionTableColumn;
    public Button deleteButton;
    public Button detailsButton;
    public MenuButton collectionTypeMenuButton;;
    public TextField searchTextField;
    public MenuItem nameCollectionItem;

    public MenuItem typeCollectionItem;
    public MenuItem amountCollectionItem;

    public TableColumn<Collection, Integer> totalAppsPartCol;
    private final List<Collection> collectionList = collectionService.findAll();
    public HBox searchContainer;
    public DatePicker deadlinePicker;

    @FXML
    public void initialize() {
        fillTableViewWithData(collectionService.findAll());
        enableDoubleClickForViewDetails();
        wrapMenuItem(new ArrayList<>(List.of(nameCollectionItem, typeCollectionItem, amountCollectionItem)));
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Collection> filterList = new ArrayList<>();
            handleFilter(filterList, newValue);
            fillTableViewWithData(filterList);
        });
        collectionTypeMenuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(collectionTypeMenuButton.getText().equals("Deadline Payment")){
                searchContainer.setVisible(false);
                searchTextField.setText("");
                deadlinePicker.setVisible(true);
                for (Collection collection : collectionList){
                }
            }else {

            }
        });
    }
    public void wrapMenuItem(List<MenuItem> list){
        for(MenuItem menuItem : list){
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    collectionTypeMenuButton.setText(menuItem.getText());
                    if(searchTextField.getText().isEmpty()){
                        fillTableViewWithData(collectionList);
                    }
                    else {
                        List<Collection> filterList = new ArrayList<>();
                        handleFilter(filterList, searchTextField.getText());
                        fillTableViewWithData(filterList);
                    }
                }
            });
        }
    }
    public void handleFilter(List<Collection> filterList, String newValue){
        switch (collectionTypeMenuButton.getText()){
            case "Name":
                for(Collection collection : collectionList){
                    if(collection.getName().contains(newValue)){
                        filterList.add(collection);
                    }
                }
                break;
            case "Type":
                for(Collection collection : collectionList){
                    if(collection.getType().toString().toLowerCase().contains(newValue.toLowerCase())){
                        filterList.add(collection);
                    }
                }
                break;
            case "Amount":
                Double amount = Double.parseDouble(newValue);
                for(Collection collection : collectionList){
                    if(amount.equals(collection.getAmount())){
                        filterList.add(collection);
                    }
                }
                break;
            default:
                filterList.addAll(collectionList);
                break;
        }
    }
    private void fillTableViewWithData(List<Collection> collectToShow) {
        collectionsTableView.setItems(FXCollections.observableList(collectToShow));
        nameTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        typeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        totalAppsPartCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartmentCollections().size()));
        amountTableColumn.setCellValueFactory(cellData -> switch (cellData.getValue().getType()) {
            case SERVICE_FEE, MANAGEMENT_FEE -> new SimpleObjectProperty<>(cellData.getValue().getAmount() + "/m2");
            default -> new SimpleObjectProperty<>(cellData.getValue().getAmount().toString());
        });
        descriptionTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
    }

    private void enableDoubleClickForViewDetails() {
        collectionsTableView.setRowFactory(tableView -> {
            TableRow<Collection> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    detailsButtonOnAction();
                }
            });
            return row;
        });
    }

    public void newButtonOnAction() {
        MenuViewManager.switchView(MenuView.COLLECTION_FORM);
    }

    public void deleteButtonOnAction() {
        Collection collection = collectionsTableView.getSelectionModel().getSelectedItem();
        if (collection != null) {
            collectionsTableView.getItems().remove(collection);
            collectionService.remove(collection);
        }
    }
    public void detailsButtonOnAction() {
        Collection collection = collectionsTableView.getSelectionModel().getSelectedItem();
        System.out.println(collection);
        if (collection != null) {
            CollectionReportController collectionReportController
                    = (CollectionReportController) MenuViewManager.switchView(MenuView.COLLECTION_REPORT);
            collectionReportController.initializeData(collection);
        }
    }
}

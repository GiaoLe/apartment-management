package com.example.demo.controller;

import com.example.demo.dao.ApartmentCollection;
import com.example.demo.dao.Collection;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectionReportController {

    public TableView<ApartmentCollection> collectionReportTableView;
    public TableColumn<ApartmentCollection, String> apartmentIDCol;
    public TableColumn<ApartmentCollection, String> hostNameCol;
    public TableColumn<ApartmentCollection, Integer> totalResCol;
    public TableColumn<ApartmentCollection, Double> amountTableColumn;
    public TableColumn<ApartmentCollection, Boolean> isPaidTableColumn;
    public TableColumn<ApartmentCollection, Date> deadlinePayment;

    public TextField collectionName;
    public TextField searchTextField;
    public MenuItem amountItem;
    public MenuItem apartmentIDItem;
    public MenuItem hostNameItem;
    public MenuItem isPaidItem;
    public MenuButton residentMenuButton;
    public MenuItem falseItem;
    public MenuButton isPaidMenuButton;
    public MenuItem trueItem;
    public HBox searchContainer;
    public Button backBtn;
    public Boolean switchViewFlag = false;
    public ApartmentCollection apartmentCollectionAfterUpdate;

    public void initializeData(Collection collection) {
        collectionName.setText(collection.getName());
        selectedItem(new ArrayList<>(List.of(amountItem, apartmentIDItem, hostNameItem, isPaidItem)), residentMenuButton);
        selectedItem(new ArrayList<>(List.of(falseItem, trueItem)), isPaidMenuButton);
        for (ApartmentCollection apartmentCollection : collection.getApartmentCollections()){
            System.out.println(apartmentCollection.getApartment().getResidents().size());
        }
        collectionReportTableView.setItems(FXCollections.observableList(collection.getApartmentCollections()));
        apartmentIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getId()));
        deadlinePayment.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadlinePayment()));
        totalResCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getResidents().size()));
        amountTableColumn.setCellValueFactory(cellData -> {
            ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
            double apartmentArea = apartmentService.findByID(cellData.getValue().getApartment().getId()).getArea();
            Double amount =
                    switch (collection.getType()) {
                        case SERVICE_FEE, MANAGEMENT_FEE -> collection.getAmount() * apartmentArea;
                        default -> collection.getAmount();
                    };
            return new SimpleObjectProperty<>(amount);
        });

        isPaidTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isPaid()));
        isPaidTableColumn.setCellFactory(column -> {
            return new TextFieldTableCell<>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        if (item) {
                            getStyleClass().add("paid");
                            getStyleClass().add("state-apartment-design");

                        } else {
                            getStyleClass().add("notPaid");
                            getStyleClass().add("state-apartment-design");

                        }
                    }
                }
            };
        });
        residentMenuButton.showingProperty().addListener(e -> {
            if (residentMenuButton.getText().equals("Is paid")){
                List<ApartmentCollection> filterList = new ArrayList<>();
                searchContainer.setVisible(false);
                isPaidMenuButton.setVisible(true);
                handleIsPaidFilter(filterList,  collection.getApartmentCollections());
                isPaidMenuButton.showingProperty().addListener(e1 -> {
                    List<ApartmentCollection> isPaidFilterList = new ArrayList<>();

                    handleIsPaidFilter(isPaidFilterList, collection.getApartmentCollections());
                    collectionReportTableView.setItems(FXCollections.observableList(isPaidFilterList));
                });
            }else{
                searchContainer.setVisible(true);
                isPaidMenuButton.setVisible(false);
                if(!searchTextField.getText().isEmpty()){
                    List<ApartmentCollection> filterList = new ArrayList<>();
                    handleFilter(filterList, searchTextField.getText(), collection.getApartmentCollections());
                    collectionReportTableView.setItems(FXCollections.observableList(filterList));
                }else{
                    collectionReportTableView.setItems(FXCollections.observableList(collection.getApartmentCollections()));
                }
            }
        });
        if (searchContainer.visibleProperty().getValue()){
            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<ApartmentCollection> filterList = new ArrayList<>();
                handleFilter(filterList, newValue, collection.getApartmentCollections());
                collectionReportTableView.setItems(FXCollections.observableList(filterList));
                apartmentIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getId()));
                totalResCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getResidents().size()));
                amountTableColumn.setCellValueFactory(cellData -> {
                    ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
                    double apartmentArea = apartmentService.findByID(cellData.getValue().getApartment().getId()).getArea();
                    Double amount =
                            switch (collection.getType()) {
                                case SERVICE_FEE, MANAGEMENT_FEE -> collection.getAmount() * apartmentArea;
                                default -> collection.getAmount();
                            };
                    return new SimpleObjectProperty<>(amount);
                });

            });
        }

    }
    public void selectedItem(List<MenuItem> menuItemList, MenuButton menuButton){
        for(MenuItem menuItem : menuItemList){
            menuItem.setOnAction(e -> {
                menuButton.setText(menuItem.getText());
            });
        }
    }
    public void backButtonOnAction() {
        if(switchViewFlag){
            MenuViewManager.switchViewFromCollectionReportToApartmentDetail(MenuView.APARTMENT_LIST, apartmentCollectionAfterUpdate);
        }else {
            MenuViewManager.switchView(MenuView.COLLECTION_LIST);
        }
    }
    public void handleIsPaidFilter(List<ApartmentCollection> filterList, List<ApartmentCollection> apartmentCollectionList){
        switch (isPaidMenuButton.getText()){
            case "False":
                for (ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if (!apartmentCollection.isPaid()){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            case "True":
                for (ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if (apartmentCollection.isPaid()){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            default:
                filterList.addAll(apartmentCollectionList);
                break;
        }
    }
    public void handleFilter(List<ApartmentCollection> filterList, String newValue, List<ApartmentCollection> apartmentCollectionList){
        switch (residentMenuButton.getText()){
            case "ApartmentID":
                for(ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if(apartmentCollection.getApartment().getId().contains(newValue)){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            case "Host name":
                for(ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if(apartmentCollection.getApartment().toString().toLowerCase().contains(newValue.toLowerCase())){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            case "Amount":
                Double amount = Double.parseDouble(newValue);
                for(ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if(amount.equals(apartmentCollection.getCollection().getAmount())){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            default:
                filterList.addAll(apartmentCollectionList);
                break;
        }
    }
}

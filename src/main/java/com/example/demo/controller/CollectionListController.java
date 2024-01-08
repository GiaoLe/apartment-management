package com.example.demo.controller;

import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.dao.Resident;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CollectionListController {
    private final CollectionService collectionService = new CollectionService(new CollectionRepository());
    public Button newButton;
    public TableView<Collection> collectionsTableView;
    public TableColumn<Collection, String> nameTableColumn;
    public TableColumn<Collection, String> typeTableColumn;
    public TableColumn<Collection, String> amountTableColumn;
    public TableColumn<Collection, String> descriptionTableColumn;
    public Button deleteButton;
    public Button detailsButton;
    public MenuButton collectionTypeMenuButton;;
    public TextField searchTextField;
    public MenuItem nameCollectionItem;

    public MenuItem typeCollectionItem;
    public MenuItem amountCollectionItem;

    public TableColumn<Collection, Integer> totalBills;
    private final List<Collection> collectionList = collectionService.findAll();
    public HBox searchContainer;
    public DatePicker deadlinePicker;
    public AnchorPane dialogContainer;
    public AnchorPane dialogBox;
    public Button closeBtn;
    private Collection selectedCollection ;
    public TextField nameTextField;
    public TextField amountTextField;
    public MenuButton typeMenu;
    public MenuItem serviceItem;
    public MenuItem manangeItem;

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
            case "Tên phí":
                for(Collection collection : collectionList){
                    if(collection.getName().contains(newValue)){
                        filterList.add(collection);
                    }
                }
                break;
            case "Loại phí":
                for(Collection collection : collectionList){
                    if(collection.getType().toString().toLowerCase().contains(newValue.toLowerCase())){
                        filterList.add(collection);
                    }
                }
                break;
            case "Chi phí":
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
        typeTableColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getType() == CollectionType.MANAGEMENT_FEE){
                return new SimpleObjectProperty<>("Phí quản lý");
            } else if(cellData.getValue().getType() == CollectionType.SERVICE_FEE){
                return new SimpleObjectProperty<>("Phí dịch vụ");
            } else {
                return new SimpleObjectProperty<>("Phí tình nguyện");
            }
        });
        totalBills.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartmentCollections().size()));
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
    public void handleEdit() {
        dialogBox.setVisible(true);
        dialogContainer.setVisible(true);
        nameTextField.setText(selectedCollection.getName());
        if (selectedCollection.getType() == CollectionType.DONATION) {
            typeMenu.setText("Phí tình nguyện");
        } else if(selectedCollection.getType() == CollectionType.MANAGEMENT_FEE){
            typeMenu.setText("Phí quản lý");
        } else if (selectedCollection.getType() == CollectionType.SERVICE_FEE){
            typeMenu.setText("Phí dịch vụ");
        }
        amountTextField.setText(String.valueOf(selectedCollection.getAmount()));

    }
    public void handleClose (){
        dialogBox.setVisible(false);
        dialogContainer.setVisible(false);
    }
    public void deleteButtonOnAction() {
        Collection collection = collectionsTableView.getSelectionModel().getSelectedItem();
        if (collection == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Hãy chọn phí để xem thông tin chi tiết");
            alert.showAndWait();
        }else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa ?");
            alert.setContentText("Nhấn OK để xóa hoặc Cancel để hủy");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                        collectionsTableView.getItems().remove(collection);
                        collectionService.remove(collection);
                }
            });
        }

    }
    public void detailsButtonOnAction() {
        Collection collection = collectionsTableView.getSelectionModel().getSelectedItem();
        selectedCollection = collection;
        if (collection != null) {
            CollectionReportController collectionReportController
                    = (CollectionReportController) MenuViewManager.switchView(MenuView.COLLECTION_REPORT);
            collectionReportController.initializeData(collection);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Hãy chọn phí để xem thông tin chi tiết");
            alert.showAndWait();
        }
    }
}

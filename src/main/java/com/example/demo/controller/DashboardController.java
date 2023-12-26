package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dao.Collection;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
import com.example.demo.service.ResidentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class DashboardController {
    public PieChart stateChart;
    public PieChart typeChart;

    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    private final CollectionService collectionService = new CollectionService(new CollectionRepository());
    public Label appsStateLabel;
    public Label appsTypeLabel;
    private final List<Apartment> apartments = new ArrayList<>(apartmentService.findAll());
    private final List<Resident> residents = new ArrayList<>(residentService.findAll());
    private final List<Collection> collections = new ArrayList<>(collectionService.findAll());
    public PieChart residentChart;
    public MenuButton selectedMenuButton;
    public MenuItem menuItem1;
    public MenuItem menuItem2;
    public AnchorPane infoContainer;
    public TableColumn<ObservableMap<String, String>, String> curCol;
    public TableColumn<ObservableMap<String, String>, String> expCol;
    public TableView<ObservableMap<String, String>> feeTableView;
    public TableColumn<ObservableMap<String, String>, String> monthCol;

    public TableColumn<ObservableMap<String, String>, String> paidCol;
    public TableColumn<ObservableMap<String, String>, String> unpaidCol;
    public AnchorPane feeContainer;
    private List<ObservableMap<String, String>> monthPaid = new ArrayList<>();
    public TableColumn<ObservableMap<String, String>, String> totalResCol;
    public MenuButton chooseFeeButton;
    public MenuItem donateItem;
    public MenuItem manageItem;
    public MenuItem serviceItem;
    public MenuButton typeFeeButton;
    @FXML
    public void initialize(){
        handleStateChart();
        handleTypeChart();
        handleResidentChart();
        selectItem(List.of(menuItem1, menuItem2));
        setItemForFeeMenuButton();
        chooseFeeButton.textProperty().addListener((observable, oldValue, newValue) -> {
            monthPaid.clear();
            handleUpdateDateTableView(newValue);

        });
    }
    public void handleStateChart(){

        int maintenanceCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.MAINTENANCE.equals(apartment.getState()));
        int availableCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.AVAILABLE.equals(apartment.getState()));
        int reserveCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.RESERVED.equals(apartment.getState()));
        int occupiedCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.OCCUPIED.equals(apartment.getState()));
        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Maintenance", maintenanceCount),
                new PieChart.Data("Available", availableCount),
                new PieChart.Data("Reserve", reserveCount),
                new PieChart.Data("Occupied", occupiedCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " amount: ", data.pieValueProperty()
                )
        ));
        stateChart.setLegendVisible(false);
        stateChart.getData().addAll(totalAppsData);
    }
    public void selectItem(List<MenuItem> list){
        for (MenuItem menuItem : list){
            menuItem.setOnAction(e -> {
                selectedMenuButton.setText(menuItem.getText());
                if(Objects.equals(menuItem.getText(), "Information of Apartment")){
                    infoContainer.setVisible(true);
                    feeContainer.setVisible(false);
                }
                else {
                    feeContainer.setVisible(true);
                    infoContainer.setVisible(false);
                }
            });
        }
    }
    public void handleTypeChart(){
        int penthouseCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.PENTHOUSE.equals(apartment.getType()));
        int studioCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.STUDIO.equals(apartment.getType()));
        int triplexCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.TRIPLEX.equals(apartment.getType()));
        int duplexCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.DUPLEX.equals(apartment.getType()));
        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Penthouse", penthouseCount),
                new PieChart.Data("Studio", studioCount),
                new PieChart.Data("Triplex", triplexCount),
                new PieChart.Data("Duplex", duplexCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " amount: ", data.pieValueProperty()
                )
        ));
        typeChart.setLegendVisible(false);
        typeChart.getData().addAll(totalAppsData);
    }
    public void handleResidentChart(){
        int femaleCount = CollectionUtils.countMatches(residents, Resident::getGender);
        int maleCount = CollectionUtils.countMatches(residents, resident -> !resident.getGender());
        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Female", femaleCount),
                new PieChart.Data("Male", maleCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " amount: ", data.pieValueProperty()
                )
        ));
        residentChart.setLegendVisible(false);
        residentChart.getData().addAll(totalAppsData);
    }
    public void setItemForFeeMenuButton(){
        for (Collection collection : collections){
            chooseFeeButton.getItems().add(
                    new MenuItem(collection.getName())
            );
        }
        List<MenuItem> menuItemList = chooseFeeButton.getItems();
        for (MenuItem menuItem : menuItemList){
            menuItem.setOnAction(e -> {
                chooseFeeButton.setText(menuItem.getText());
            });
        }
    }
    public void handleUpdateDateTableView(String collectionName){
        updateData(collectionName);
        feeTableView.setItems(FXCollections.observableList(monthPaid));
        monthCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("month")));
        expCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("expFee")));
        curCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("receivedFee")));
        paidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("paidRes")));
        unpaidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("unpaidRes")));
        totalResCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalRes")));
    }
    public void updateData(String collectionName){
        List<Apartment> apartments1 = new ArrayList<>(apartmentService.findAll());
        for(int i = 0 ; i <= 11 ; i++){
            ObservableMap<String, String> observableMap = FXCollections.observableHashMap();
            initMap(observableMap);
            for (Apartment apartment : apartments1){
                for (ApartmentCollection apartmentCollection : apartment.getApartmentCollectionList()){
                    if (apartmentCollection.getDeadlinePayment().getMonth() == i){
                        updateFeeTableView(collectionName, apartmentCollection, observableMap);
                    }
                    Calendar calendarForMonth = Calendar.getInstance();
                    calendarForMonth.set(Calendar.MONTH, i);
                    observableMap.put("month", String.valueOf(calendarForMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())));
                }
            }
            monthPaid.add(observableMap);
        }

    }

    private void updateFeeTableView(String collectionName, ApartmentCollection apartmentCollection, ObservableMap<String, String> observableMap) {
        if(apartmentCollection.getCollection().getName().equals(collectionName)){
            observableMap.compute("expFee", (key, oldValue) -> String.valueOf(Double.parseDouble(oldValue) + apartmentCollection.getCollection().getAmount()*apartmentCollection.getApartment().getArea()));
            observableMap.compute("totalRes", (key, oldvalue) -> String.valueOf(Integer.parseInt(oldvalue) + 1));
            if (apartmentCollection.isPaid()){
                observableMap.compute("receivedFee", (key, oldValue) -> String.valueOf(Double.parseDouble(oldValue) + apartmentCollection.getCollection().getAmount() * apartmentCollection.getApartment().getArea()));
                observableMap.compute("paidRes", (key, oldValue) -> String.valueOf(Integer.parseInt(oldValue) + 1));
            } else {
                observableMap.compute("unpaidRes", (key, oldValue) -> String.valueOf(Integer.parseInt(oldValue) + 1));
            }
        }
    }

    public void initMap(ObservableMap<String, String> observableMap){
        observableMap.put("expFee", String.valueOf(0.0));
        observableMap.put("receivedFee", String.valueOf(0.0));
        observableMap.put("totalRes", String.valueOf(0));
        observableMap.put("paidRes", String.valueOf(0));
        observableMap.put("unpaidRes", String.valueOf(0));
    }
}

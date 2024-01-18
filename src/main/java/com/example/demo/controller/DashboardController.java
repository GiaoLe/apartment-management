package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dao.Collection;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.HibernateUtility;
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

import java.time.LocalDate;
import java.time.Year;
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
    private final List<Collection> serviceCollections = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection where type = :type", Collection.class)
            .setParameter("type", CollectionType.SERVICE_FEE)
            .getResultList());
    private final List<Collection> manageCollections = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection where type = :type", Collection.class)
            .setParameter("type", CollectionType.MANAGEMENT_FEE)
            .getResultList());
    private final List<Collection> donateCollections = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection where type = :type", Collection.class)
            .setParameter("type", CollectionType.DONATION)
            .getResultList());
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
    public MenuItem revenueStatic;
    public MenuButton yearMenu;
    @FXML
    public void initialize(){
        handleStateChart();
        handleTypeChart();
        handleResidentChart();
        selectItem(List.of(menuItem1, menuItem2));
        handleSelectedTypeCollection(List.of(serviceItem, manageItem, donateItem));
        chooseFeeButton.textProperty().addListener((observable, oldValue, newValue) -> {
            yearMenu.getItems().clear();
            initializeYearMenu(newValue);
            yearMenu.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                monthPaid.clear();
                handleUpdateDateTableView(newValue, newValue1);
            });
        });

    }
    public void handleStateChart(){

        int maintenanceCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.MAINTENANCE.equals(apartment.getState()));
        int availableCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.AVAILABLE.equals(apartment.getState()));
        int reserveCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.RESERVED.equals(apartment.getState()));
        int occupiedCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentState.OCCUPIED.equals(apartment.getState()));
        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Đang sửa chữa", maintenanceCount),
                new PieChart.Data("Còn trống", availableCount),
                new PieChart.Data("Đang bảo trì", reserveCount),
                new PieChart.Data("Đang sử dụng", occupiedCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), ": ", data.pieValueProperty()
                )
        ));
        stateChart.setLegendVisible(false);
        stateChart.getData().addAll(totalAppsData);
    }
    public void selectItem(List<MenuItem> list){
        for (MenuItem menuItem : list){
            menuItem.setOnAction(e -> {
                selectedMenuButton.setText(menuItem.getText());
                if(Objects.equals(menuItem.getText(), "Thông tin chung cư")){
                    infoContainer.setVisible(true);
                    feeContainer.setVisible(false);
                }
                else if(Objects.equals(menuItem.getText(), "Thống kê doanh thu")) {
                    feeContainer.setVisible(true);
                    infoContainer.setVisible(false);
                } else {

                }
            });
        }
    }
    public void initializeYearMenu(String newValue){
        List<Integer> year = new ArrayList<>();
        List<ApartmentCollection> apartmentCollectionList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from ApartmentCollection where collection.name = :name", ApartmentCollection.class)
                .setParameter("name", newValue)
                .getResultList());
        for (ApartmentCollection apartmentCollection1 : apartmentCollectionList){
            if (!year.contains(apartmentCollection1.getDeadlinePayment().getYear() + 1900)){
                year.add(apartmentCollection1.getDeadlinePayment().getYear() + 1900);
            }
        }
        for (Integer integer : year){
            yearMenu.getItems().add(new MenuItem(String.valueOf(integer)));
        }
        for (MenuItem menuItem : yearMenu.getItems()){
            menuItem.setOnAction(e -> {
                yearMenu.setText(menuItem.getText());
            });
        }
    }
    public void handleSelectedTypeCollection (List <MenuItem> menuItemList){
        for (MenuItem menuItem : menuItemList){
            menuItem.setOnAction(e -> {
                typeFeeButton.setText(menuItem.getText());
                chooseFeeButton.getItems().clear();
                chooseFeeButton.setText("Chọn phí");
                if (typeFeeButton.getText().equals("Phí dịch vụ chung cư")){
                    for (Collection collection : serviceCollections){
                        chooseFeeButton.getItems().add(
                                new MenuItem(collection.getName())
                        );
                    }
                } else if (typeFeeButton.getText().equals("Phí quản lý chung cư")){
                    for (Collection collection : manageCollections){
                        chooseFeeButton.getItems().add(
                                new MenuItem(collection.getName())
                        );
                    }
                } else {
                    for (Collection collection : donateCollections){
                        chooseFeeButton.getItems().add(
                                new MenuItem(collection.getName())
                        );
                    }
                }
                List<MenuItem> menuItemList1 = chooseFeeButton.getItems();
                for (MenuItem menuItem1 : menuItemList1){
                    menuItem1.setOnAction(e1 -> {
                        chooseFeeButton.setText(menuItem1.getText());
                        yearMenu.setDisable(false);

                    });
                }
            });
        }
    }
    public void handleTypeChart(){
        int penthouseCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.PENTHOUSE.equals(apartment.getType()));
        int studioCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.STUDIO.equals(apartment.getType()));
        int triplexCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.TRIPLEX.equals(apartment.getType()));
        int duplexCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.DUPLEX.equals(apartment.getType()));
        int condoCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.CONDO.equals(apartment.getType()));
        int gardenCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.GARDEN.equals(apartment.getType()));
        int loftCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.LOFT.equals(apartment.getType()));
        int townHouseCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.TOWNHOUSE.equals(apartment.getType()));
        int villaCount = CollectionUtils.countMatches(apartments, apartment -> ApartmentType.VILLA.equals(apartment.getType()));

        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Penthouse", penthouseCount),
                new PieChart.Data("Studio", studioCount),
                new PieChart.Data("Triplex", triplexCount),
                new PieChart.Data("Duplex", duplexCount),
                new PieChart.Data("Condo", condoCount),
                new PieChart.Data("Garden", gardenCount),
                new PieChart.Data("Loft", loftCount),
                new PieChart.Data("TownHouse", townHouseCount),
                new PieChart.Data("Villa", villaCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), ": ", data.pieValueProperty()
                )
        ));
        typeChart.setLegendVisible(false);
        typeChart.getData().addAll(totalAppsData);
    }
    public void handleResidentChart(){
        int femaleCount = CollectionUtils.countMatches(residents, Resident::getGender);
        int maleCount = CollectionUtils.countMatches(residents, resident -> !resident.getGender());
        ObservableList<PieChart.Data> totalAppsData = FXCollections.observableArrayList(
                new PieChart.Data("Nữ", femaleCount),
                new PieChart.Data("Nam", maleCount)
        );
        totalAppsData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), ": ", data.pieValueProperty()
                )
        ));
        residentChart.setLegendVisible(false);
        residentChart.getData().addAll(totalAppsData);
    }
    public void handleUpdateDateTableView(String collectionName, String year){
        updateData(collectionName, year);
        feeTableView.setItems(FXCollections.observableList(monthPaid));
        monthCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("month")));
        expCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("expFee")));
        curCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("receivedFee")));
        paidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("paidRes")));
        unpaidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("unpaidRes")));
        totalResCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalRes")));
    }
    public void updateData(String collectionName, String year){
        List<Apartment> apartments1 = new ArrayList<>(apartmentService.findAll());
        for (int i = 0; i <= 11; i++) {
            ObservableMap<String, String> observableMap = FXCollections.observableHashMap();
            initMap(observableMap);
            Calendar calendarForMonth = Calendar.getInstance();
            calendarForMonth.set(Calendar.MONTH, i);
            observableMap.put("month", String.valueOf(calendarForMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())));
            if (i == 1){
                observableMap.put("month", "February");
            }
            for (Apartment apartment : apartments1) {
                for (ApartmentCollection apartmentCollection : apartment.getApartmentCollectionList()) {
                    if (!yearMenu.getText().equals("Chọn năm")){
                        if (apartmentCollection.getDeadlinePayment().getYear() + 1900 == Year.of(Integer.parseInt(year)).getValue()){
                            if (apartmentCollection.getDeadlinePayment().getMonth() == i) {
                                updateFeeTableView(collectionName, apartmentCollection, observableMap);
                            }
                        }
                    }
                }
            }
            monthPaid.add(observableMap);
        }
    }

    private void updateFeeTableView(String collectionName, ApartmentCollection apartmentCollection, ObservableMap<String, String> observableMap) {
        if(apartmentCollection.getCollection().getName().equals(collectionName)){
            observableMap.compute("expFee", (key, oldValue) -> String.valueOf(Double.parseDouble(oldValue) + apartmentCollection.getCollection().getAmount()*apartmentCollection.getApartment().getArea()));
            observableMap.compute("totalRes", (key, oldvalue) -> String.valueOf(Integer.parseInt(oldvalue) + 1));
            if (apartmentCollection.getState() == ApartmentCollectionState.PAID){
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
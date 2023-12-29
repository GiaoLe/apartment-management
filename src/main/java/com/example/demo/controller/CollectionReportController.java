package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dao.Collection;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentCollectionRepository;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CollectionReportController {

    public TableView<ApartmentCollection> collectionReportTableView;
    public TableColumn<ApartmentCollection, String> apartmentIDCol;
    public TableColumn<ApartmentCollection, String> hostNameCol;
    public TableColumn<ApartmentCollection, Integer> totalResCol;
    public TableColumn<ApartmentCollection, Double> amountTableColumn;
    public TableColumn<ApartmentCollection, String> isPaidTableColumn;
    public TableColumn<ApartmentCollection, Date> deadlinePayment;
    public TableColumn<Apartment, String> apartmentIDCol1;
    public TableView<Apartment> donateApartmentTableView;
    public TableColumn<Apartment, String> hostNameCol1;
    public TableColumn<Apartment, String> takePartCol;
    public TableColumn<Apartment, Integer> totalResCol1;
    public Button takePartButton;
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
    public ApartmentCollection apartmentCollection;
    public Button markAsPaidButton;
    public Label reportNameLabel;
    public Button addAppsBtn;
    public MenuItem deadlinePaymentItem;
    public MenuButton monthButton;
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    private ObservableMap<String, Integer> dateObservableMap = FXCollections.observableHashMap();
    private Collection selectedCollection = new Collection();
    public Label infoLabel;
    private Boolean addDonateAppFlag = false;
    public void initializeData(Collection collection) {
        selectedCollection = collection;
        reportNameLabel.setText("Collection's Report: " + collection.getName());
        selectedItem(new ArrayList<>(List.of(amountItem, apartmentIDItem, hostNameItem, isPaidItem, deadlinePaymentItem)), residentMenuButton);
        selectedItem(new ArrayList<>(List.of(falseItem, trueItem)), isPaidMenuButton);
        Boolean flag = false;
        for (ApartmentCollection apartmentCollection1 : collection.getApartmentCollections()){
            if (apartmentCollection1.getApartment() == null){
                flag = true;
            }
        }
        if (!flag){
            infoLabel.setVisible(false);
            collectionReportTableView.setItems(FXCollections.observableList(collection.getApartmentCollections()));
            apartmentIDCol.setCellValueFactory(cellData ->  new SimpleObjectProperty<>(cellData.getValue().getApartment().getId()));
            deadlinePayment.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadlinePayment()));
            totalResCol.setCellValueFactory(cellData ->  new SimpleObjectProperty<>(cellData.getValue().getApartment().getResidents().size()));
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
            initializeMonthMenuButton();
            isPaidTableColumn.setCellValueFactory(cellData -> cellData.getValue().isPaid() ? new SimpleObjectProperty<>("Yes") : new SimpleObjectProperty<>("No"));
            isPaidTableColumn.setCellFactory(column -> new TextFieldTableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        if (item.equals("Yes")) {
                            getStyleClass().add("paid");
                            getStyleClass().add("state-apartment-design");

                        } else {
                            getStyleClass().add("notPaid");
                            getStyleClass().add("state-apartment-design");

                        }
                    }
                }
            });
        } else {
            infoLabel.setVisible(true);
            collectionReportTableView.setVisible(false);
        }
        takePartButton.setOnMouseClicked(e -> {
            handleTakePart(collection);
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
                    updateTable(FXCollections.observableList(isPaidFilterList), collection);

                });
            } else if (residentMenuButton.getText().equals("Deadline payment")){
                searchContainer.setVisible(false);
                monthButton.setVisible(true);
            }
            else{
                searchContainer.setVisible(true);
                isPaidMenuButton.setVisible(false);
                monthButton.setVisible(false);
                if(!searchTextField.getText().isEmpty()){
                    List<ApartmentCollection> filterList = new ArrayList<>();
                    handleFilter(filterList, searchTextField.getText(), collection.getApartmentCollections());
                    updateTable(FXCollections.observableList(filterList), collection);

                }else{
                    collectionReportTableView.setItems(FXCollections.observableList(collection.getApartmentCollections()));
                }
            }
        });
        if (searchContainer.visibleProperty().getValue()){
            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<ApartmentCollection> filterList = new ArrayList<>();
                handleFilter(filterList, newValue, collection.getApartmentCollections());
                updateTable(FXCollections.observableList(filterList), collection);
            });
        }
        monthButton.showingProperty().addListener(e -> {
            List<ApartmentCollection> filterList = new ArrayList<>();
            handleSearchByMonth(filterList, monthButton.getText(), collection.getApartmentCollections());
            updateTable(FXCollections.observableList(filterList), collection);
        });

    }
    public void selectedItem(List<MenuItem> menuItemList, MenuButton menuButton){
        for(MenuItem menuItem : menuItemList){
            menuItem.setOnAction(e -> {
                menuButton.setText(menuItem.getText());
            });
        }
    }
    public void updateTable(ObservableList<ApartmentCollection> observableList, Collection collection){
        collectionReportTableView.setItems(observableList);
        apartmentIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getId()));
        totalResCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartment().getResidents().size()));
        isPaidTableColumn.setCellValueFactory(cellData -> cellData.getValue().isPaid() ? new SimpleObjectProperty<>("Yes") : new SimpleObjectProperty<>("No"));
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
        deadlinePayment.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadlinePayment()));
        hostNameCol.setCellValueFactory(cellData -> cellData.getValue().getApartment().getHost() == null ? new SimpleObjectProperty<>("Unknown") : new SimpleObjectProperty<>(cellData.getValue().getApartment().getHost().getLastName()));
    }
    public void initializeMonthMenuButton(){
        for (int i = 0 ; i <= 11 ; i++){
            Calendar calendarForMonth = Calendar.getInstance();
            calendarForMonth.set(Calendar.MONTH, i);
            monthButton.getItems().add(
                    new MenuItem(String.valueOf(calendarForMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())))
            );
            dateObservableMap.put(String.valueOf(calendarForMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())), i+1);
        }
        List <MenuItem> list = monthButton.getItems();
        for (MenuItem menuItem : list){
            menuItem.setOnAction(e -> {
                monthButton.setText(menuItem.getText());
            });
        }
    }
    public void handleSearchByMonth(List<ApartmentCollection> filterList, String month, List<ApartmentCollection> apartmentCollectionList){
        Month month1 = Month.of(dateObservableMap.get(month));

        for (ApartmentCollection apartmentCollection1 : apartmentCollectionList){
            if (apartmentCollection1.getDeadlinePayment().getMonth() == (month1.getValue() - 1)){
                filterList.add(apartmentCollection1);
            }
        }
    }
    public void backButtonOnAction() {
        if(switchViewFlag){
            MenuViewManager.switchViewFromCollectionReportToApartmentDetail(MenuView.APARTMENT_LIST, apartmentCollection);
        }else {
            MenuViewManager.switchView(MenuView.COLLECTION_LIST);
        }
    }
    public void handleIsPaidFilter(List<ApartmentCollection> filterList, List<ApartmentCollection> apartmentCollectionList){
        switch (isPaidMenuButton.getText()){
            case "No":
                for (ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if (!apartmentCollection.isPaid()){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            case "Yes":
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
            case "Deadline payment":
                for(ApartmentCollection apartmentCollection : apartmentCollectionList){
                    if(apartmentCollection.getDeadlinePayment() == java.sql.Date.valueOf(newValue)){
                        filterList.add(apartmentCollection);
                    }
                }
                break;
            default:
                filterList.addAll(apartmentCollectionList);
                break;
        }
    }

    public void markAsPaidButtonOnAction() {
        apartmentCollection = collectionReportTableView.getSelectionModel().getSelectedItem();
        apartmentCollection.setPaid(true);
        ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
        apartmentCollectionService.merge(apartmentCollection);
        collectionReportTableView.refresh();
        switchViewFlag = true;
    }
    public void handleListApartmentToDonate(){
        List<Apartment> apartments = apartmentService.findAll();
        List<Apartment> notTakePartApartment = new ArrayList<>();
        for (Apartment apartment : apartments){
            Boolean flag = false;
            for (ApartmentCollection apartmentCollection1 : apartment.getApartmentCollectionList()){
                if (Objects.equals(apartmentCollection1.getCollection().getId(), selectedCollection.getId())){
                    flag = true;
                }
            }
            if (!flag){
                notTakePartApartment.add(apartment);
            }
        }
        donateApartmentTableView.setItems(FXCollections.observableList(notTakePartApartment));
        apartmentIDCol1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        hostNameCol1.setCellValueFactory(cellData -> cellData.getValue().getHost() == null ? new SimpleObjectProperty<>("Unknown") : new SimpleObjectProperty<>(cellData.getValue().getHost().getLastName()));
        totalResCol1.setCellValueFactory(celldata -> new SimpleObjectProperty<>(celldata.getValue().getResidents().size()));
        takePartCol.setCellValueFactory(celldata -> {
            for (ApartmentCollection apartmentCollection1 : celldata.getValue().getApartmentCollectionList()){
                if (Objects.equals(apartmentCollection1.getCollection().getId(), selectedCollection.getId())){
                    return new SimpleObjectProperty<>("Yes");
                }
            }
            return new SimpleObjectProperty<>("No");
        });
        takePartCol.setCellFactory(column -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.equals("No")) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("no");

                    } else if (item.equals("Yes")) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("yes");

                    }
                }
            }
                });
    }
    public void handleAddApartment(){
        donateApartmentTableView.setVisible(true);
        collectionReportTableView.setVisible(false);
        markAsPaidButton.setVisible(false);
        takePartButton.setVisible(true);
        infoLabel.setVisible(false);
        handleListApartmentToDonate();
    }
    public void handleTakePart(Collection collection){
        Apartment apartment = donateApartmentTableView.getSelectionModel().getSelectedItem();
        ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
        Date date = collection.getApartmentCollections().getFirst().getDeadlinePayment();
        apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date));
        if (!addDonateAppFlag){
            for (ApartmentCollection apartmentCollection1 : collection.getApartmentCollections()){
                if (apartmentCollection1.getApartment() == null){
                    System.out.println(true);
                    apartmentCollectionService.remove(apartmentCollection1);
                    addDonateAppFlag = true;
                }
            }
        }
        handleListApartmentToDonate();

    }
}

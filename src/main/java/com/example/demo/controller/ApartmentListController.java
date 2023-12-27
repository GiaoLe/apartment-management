package com.example.demo.controller;
import com.example.demo.dao.*;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import com.example.demo.repository.HibernateUtility;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ApartmentListController {
    public Label floorText;


    public Label tableHeader;
    public Button backBtn;
    public TableView<ObservableMap<String, String>> floorTableView;
    public TableColumn<ObservableMap<String, String>, String> occupiedColumn;
    public TableColumn<ObservableMap<String, String>, String> residentsColumn;
    public TableColumn<ObservableMap<String, String>, String> totalColumn;
    public TableColumn<ObservableMap<String, String>, String> floorColumn;
    public TableColumn<ObservableMap<String, String>, String> nAvailableColumn;
    public TableView<Apartment> apartmentTableView;
    public TableColumn<ObservableMap<String, String>, String> availableColumn;
    private ObservableList<ObservableMap<String, String>> floorList;

    public AnchorPane dialogContainer;
    public AnchorPane dialogBox;
    public MenuItem availableItem;
    public MenuItem duplexItem;
    public MenuItem maintainingItem;
    public MenuItem occupiedItem;
    public MenuItem penthouseItem;
    public MenuItem studioItem;
    public MenuItem triplexItem;
    public MenuItem reservedItem;
    public TextField apartmentIDFilter;
    public TextField hostNameFilter;
    public MenuButton stateMenu;
    public MenuButton typeMenu;

    public Button closeDialogBtn;
    public TableColumn<Apartment, String> actionsCol;
    public TableColumn<Apartment, String> apartmentIdCol;
    public TableColumn<Apartment, Double> areaCol;
    public TableColumn<Apartment, String> hostNameCol;
    public TableColumn<Apartment, String> stateCol;
    public TableColumn<Apartment, String> totalResidentsCol;
    public TableColumn<Apartment, String> typeCol;

    public ObservableMap<String, String> selectedFloor = FXCollections.observableHashMap();
    public Button submitFilter;
    public Button apartmentInfoClosebtn;
    public AnchorPane apartmentInfoDialog;
    public TableColumn<Resident, String> email;
    public TableColumn<Resident, String> lastName;
    public TableColumn<Resident, String> nationalID;
    public TableColumn<Resident, String> phoneNumber;
    public TableColumn<Resident, String> residentID;
    public TableView<Resident> residentTableView;
    public Button addResBtn;
    public Button delResBtn;
    public Button editResBtn;
    public TextField apartmentIDFilter1;
    public MenuButton hostNameFilter1;
    public MenuItem availableItem1;
    public MenuItem duplexItem1;
    public MenuItem occupiedItem1;
    public MenuItem maintainingItem1;
    public MenuItem penthouseItem1;
    public MenuItem reservedItem1;
    public MenuButton stateMenu1;
    public MenuItem studioItem1;
    public MenuItem triplexItem1;
    public MenuButton typeMenu1;
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    private static final int TOTAL_FLOOR = 50;
    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    public Button addNewApartment;
    public TableColumn<ApartmentCollection, Double> amountCollectionCol;
    public TableColumn<ApartmentCollection, Integer> collectionIDCol;

    public TableView<ApartmentCollection> collectionTableView;
    public TableColumn<ApartmentCollection, Date> deadlinePaymentCollectionCol;
    public TableColumn<ApartmentCollection, String> nameCollectionCol;
    public TableColumn<ApartmentCollection, CollectionType> typeCollectionCol;
    public TableColumn<ApartmentCollection, Boolean> isPaidCol;
    public Apartment selectedApartment;
    public MenuItem apartmentIDItem;
    public MenuButton apartmentMenuButton;
    public MenuItem areaItem;
    public MenuItem hostNameItem;
    public TextField searchTextField;
    public MenuItem typeItem;
    public MenuItem stateItem;

    public void updateFloorDetails(List<Apartment> apartmentsToShowFloorList) {
        floorList = FXCollections.observableArrayList();
        for(int i = 1; i <= TOTAL_FLOOR; i++){
            ObservableMap<String, String> floorDetail = FXCollections.observableHashMap();
            int numberOfApartment;
            for(Apartment apartment : apartmentsToShowFloorList){
                if(apartment.getFloor() == i){
                    if(floorDetail.get("floor") == null){
                        floorDetail.put("floor", String.valueOf(i));
                        floorDetail.put("totalApartments", "1");
                        floorDetail.put("totalResidents", String.valueOf(apartment.getResidents().size()));
                        updateAppsState(apartment, floorDetail);
                    }
                    else {
                        numberOfApartment = Integer.parseInt(floorDetail.get("totalApartments")) + 1;
                        long totalResidents = Long.parseLong(floorDetail.get("totalResidents"));
                        totalResidents += apartment.getResidents().size();
                        floorDetail.put("totalApartments", String.valueOf(numberOfApartment));
                        floorDetail.put("totalResidents", String.valueOf(totalResidents));
                        if (apartment.getState() == ApartmentState.AVAILABLE) {
                            floorDetail.put("availableApartments", String.valueOf(Integer.parseInt(floorDetail.get("availableApartments")) + 1));
                        } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                            floorDetail.put("oApartments", String.valueOf(Integer.parseInt(floorDetail.get("oApartments")) + 1));
                        } else {
                            floorDetail.put("nAAvailableApartments", String.valueOf(Integer.parseInt(floorDetail.get("nAAvailableApartments")) + 1));
                        }
                    }
                }
            }
            if(!floorDetail.isEmpty()){
                floorList.add(floorDetail);
            }
        }
    }
    private void updateAppsState(Apartment apartment, ObservableMap<String, String> No) {
        if (apartment.getState() == ApartmentState.AVAILABLE) {
            No.put("availableApartments", "1");
            No.put("nAAvailableApartments", "0");
            No.put("oApartments", "0");
        } else if (apartment.getState() == ApartmentState.OCCUPIED) {
            No.put("availableApartments", "0");
            No.put("nAAvailableApartments", "0");
            No.put("oApartments", "1");
        } else {
            No.put("availableApartments", "0");
            No.put("nAAvailableApartments", "1");
            No.put("oApartments", "0");
        }
    }

    public void showFloorList(List<Apartment> apartmentsToShowFloorList){
        updateFloorDetails(apartmentsToShowFloorList);
        floorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("floor")));
        totalColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalApartments")));
        availableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("availableApartments")));
        nAvailableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("nAAvailableApartments")));
        occupiedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("oApartments")));
        residentsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalResidents")));
        floorTableView.setItems(floorList);
    }
    public void initialize() {
        List<Apartment> apartmentsToShowFloorList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        showFloorList(apartmentsToShowFloorList);
        selectedType(List.of(apartmentIDItem, hostNameItem, stateItem, typeItem, areaItem), apartmentMenuButton);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(apartmentMenuButton.visibleProperty().getValue()){
               if(searchTextField.getText().isEmpty()){
                   showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
               }else {
                   List<Apartment> filterList = new ArrayList<>();
                   List<Apartment> secondFilterList = new ArrayList<>();
                    for(Apartment apartment : apartmentsToShowFloorList){
                        if(apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))){
                            filterList.add(apartment);
                        }
                    }
                   handleFilter(secondFilterList, newValue, filterList);
                   showApartments(FXCollections.observableList(secondFilterList));
               }
            } else {
                if(searchTextField.getText().isEmpty()){
                    floorTableView.setItems(FXCollections.observableList(floorList));

                }else {
                    ObservableList<ObservableMap<String, String>> filterList = FXCollections.observableArrayList();
                    for(ObservableMap<String, String> floor : floorList){
                        if(floor.get("floor").equals(searchTextField.getText())){
                            filterList.add(floor);
                        }
                    }
                    floorTableView.setItems(filterList);
                }
            }
        });
        apartmentMenuButton.showingProperty().addListener(e -> {
            List<Apartment> filterList = new ArrayList<>();
            List<Apartment> secondFilterList = new ArrayList<>();
            if(searchTextField.getText().isEmpty()){
                showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
            }else {
                for(Apartment apartment : apartmentsToShowFloorList){
                    if(apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))){
                        filterList.add(apartment);
                    }
                }
                handleFilter(secondFilterList, searchTextField.getText(), filterList);
                showApartments(FXCollections.observableList(secondFilterList));
            }
        });
        floorTableView.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                searchTextField.setPromptText("Floor...");
                apartmentMenuButton.setVisible(false);
                searchTextField.setText("");
            }else {
                searchTextField.setPromptText("Search...");
                apartmentMenuButton.setVisible(true);
                searchTextField.setText("");

            }
        });
    }
    public void handleFilter(List<Apartment> filterList, String newValue, List<Apartment> apartments){
        if(apartmentMenuButton.visibleProperty().get()){
            switch (apartmentMenuButton.getText()){
                case "Apartment ID":
                    for(Apartment apartment : apartments){
                        if(apartment.getId().contains(newValue)){
                            filterList.add(apartment);
                        }
                    }
                    break;
                case "State":
                    for(Apartment apartment : apartments){
                        if(String.valueOf(apartment.getState()).toLowerCase().contains(newValue.toLowerCase())){
                            filterList.add(apartment);
                        }
                    }
                    break;
                case "Type":
                    for(Apartment apartment : apartments){
                        if(String.valueOf(apartment.getType()).toLowerCase().contains(newValue.toLowerCase())){
                            filterList.add(apartment);
                        }
                    }
                    break;
                case "Area":
                    for(Apartment apartment : apartments){
                        if(apartment.getArea() == Double.parseDouble(newValue)){
                            filterList.add(apartment);
                        }
                    }
                    break;
                default:
                    filterList.addAll(apartments);
                    break;
            }
        }else {
            for(Apartment apartment : apartments){
                if(apartment.getId().contains(newValue)){
                    filterList.add(apartment);
                }
            }
        }
    }
    public void selectedFloor() {
        floorTableView.setOnMouseClicked(event -> {
            selectedFloor = floorTableView.getSelectionModel().getSelectedItem();
            showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
            handleClickedFloor();
        });
        backBtn.setOnMouseClicked(event -> {
            floorTableView.setVisible(true);
            apartmentTableView.setVisible(false);
            backBtn.setVisible(false);
        });
    }
    public void handleAddNewApartment () {
        MenuViewManager.switchView(MenuView.APARTMENT_FORM);

    }
    public void showFloorDetail(int floor) {
        List<Apartment> floorApartments = new ArrayList<>();
        List<Apartment> apartmentsToShowFloorList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        for(Apartment apartment : apartmentsToShowFloorList){
            if(apartment.getFloor() == floor){
                floorApartments.add(apartment);
            }

        }
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(floorApartments);
        showApartments(apartmentObservableList);
    }

    public void handleClickedFloor(){
        floorTableView.setVisible(false);
        apartmentTableView.setVisible(true);
        backBtn.setVisible(true);

    }
    public void toggleFilter(){
        if (closeDialogBtn.visibleProperty().getValue()){
            closeDialogBtn.setOnMouseClicked(event -> {
                dialogContainer.setVisible(false);
                dialogBox.setVisible(false);
            });
        }
    }
    public void selectedType(List<MenuItem> listItems, MenuButton typeMenu){
        for (MenuItem selectedItems : listItems){
            selectedItems.setOnAction(event -> typeMenu.setText(selectedItems.getText()));
        }
    }
    public void updateData(){
        List<Apartment> newApartmentList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
        floorList.clear();
        showFloorList(newApartmentList);
    }
    public void updateResidentListsInApartment (Apartment apartment) {
        ObservableList<Resident> residentObservableList = FXCollections.observableList(apartment.getResidents());
        residentID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getId())));
        phoneNumber.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        lastName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
        email.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        nationalID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNationalID()));
        residentTableView.setItems(residentObservableList);
    }

    public void updateCollectionListsInApartment(Apartment apartment){
        List<ApartmentCollection> apartmentCollectionList = new ArrayList<>(apartment.getApartmentCollectionList());

        collectionIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getId()));
        nameCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getName()));
        typeCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getType()));
        amountCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getAmount()));
        isPaidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isPaid()));
        isPaidCol.setCellFactory(column -> new TextFieldTableCell<>() {
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
        });
        collectionTableView.setOnMouseClicked(e -> {
            if(e.getClickCount() >= 2){
                MenuViewManager.switchViewToShowCollectionDetail(MenuView.COLLECTION_REPORT, collectionTableView.getSelectionModel().getSelectedItem(), selectedApartment);
            }
        });
        collectionTableView.setItems(FXCollections.observableList(apartmentCollectionList));
    }
    public void showApartments(ObservableList<Apartment> index){
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        totalResidentsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidents().size()).asString());
        stateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()).asString());
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()).asString());
        hostNameCol.setCellValueFactory(cellData -> cellData.getValue().getHost() == null ? new SimpleObjectProperty<>("Unknown") : new SimpleObjectProperty<>(cellData.getValue().getHost().getLastName()));
        areaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        stateCol.setCellFactory(column -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.equals(ApartmentState.AVAILABLE.toString())) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("available-state");

                    } else if (item.equals(ApartmentState.OCCUPIED.toString())) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("occupied-state");

                    } else if (item.equals(ApartmentState.MAINTENANCE.toString())) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("maintenance-state");

                    } else if (item.equals(ApartmentState.RESERVED.toString())) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("reserved-state");


                    }
                }
            }
        });
        Callback<TableColumn<Apartment, String>, TableCell<Apartment, String>> cellFactory = (TableColumn<Apartment, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button editBtn = new Button();
                    Button deleteBtn = new Button();
                    ImageView imageView = new ImageView();
                    ImageView imageView1 = new ImageView();
                    String imagePath = Objects.requireNonNull(getClass().getResource("/images/pencil.png")).toExternalForm();
                    String imagePath1 = Objects.requireNonNull(getClass().getResource("/images/delete.png")).toExternalForm();
                    Image image = new Image(imagePath);
                    Image image1 = new Image(imagePath1);
                    imageView.setImage(image);
                    imageView1.setImage(image1);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    imageView1.setFitWidth(20);
                    imageView1.setFitHeight(20);
                    editBtn.setGraphic(imageView);
                    deleteBtn.setGraphic(imageView1);
                    int rowIndex = getIndex();
                    if (rowIndex % 2 == 0) {
                        editBtn.setStyle("-fx-background-color: #fff;" + "-fx-cursor:HAND;");
                        deleteBtn.setStyle("-fx-background-color: #fff;" + "-fx-cursor:HAND;");
                        editBtn.setOnMouseExited(e -> editBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                        ));
                        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #dcdcdc;"));
                        editBtn.setOnMousePressed(e -> editBtn.setStyle("-fx-background-color: #868686;"));
                        deleteBtn.setOnMousePressed(e -> deleteBtn.setStyle("-fx-background-color: #868686FF;"));
                        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                        ));
                        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #dcdcdc;"));
                    } else {
                        editBtn.setStyle("-fx-background-color: #f0f0f0;" + "-fx-cursor:HAND;");
                        deleteBtn.setStyle("-fx-background-color: #f0f0f0;" + "-fx-cursor:HAND;");
                        editBtn.setOnMouseExited(e -> editBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #f0f0f0;"
                        ));
                        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #d7d7d7;"));
                        editBtn.setOnMousePressed(e -> editBtn.setStyle("-fx-background-color: #BDBDBDFF;"));
                        deleteBtn.setOnMousePressed(e -> deleteBtn.setStyle("-fx-background-color: #BDBDBDFF;"));
                        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #f0f0f0;"
                        ));
                        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #d7d7d7;"));

                    }


                    deleteBtn.setOnMouseClicked(e -> {
                        Apartment apartment = getTableView().getItems().get(getIndex());
                        apartmentService.remove(apartment);
                        updateData();
                    });
                    editBtn.setOnMouseClicked(e -> {
                        dialogContainer.setVisible(true);
                        apartmentInfoDialog.setVisible(true);
                        apartmentInfoClosebtn.setOnMouseClicked(e1 -> {
                            dialogContainer.setVisible(false);
                            apartmentInfoDialog.setVisible(false);
                        });
                        Apartment apartment = getTableView().getItems().get(getIndex());
                        selectedApartment = apartment;
                        updateCollectionListsInApartment(apartment);
                        apartmentIDFilter1.setText(apartment.getId());
                        stateMenu1.setText(String.valueOf(apartment.getState()));
                        typeMenu1.setText(String.valueOf(apartment.getType()));
                        hostNameFilter1.setText(apartment.getHost() == null ? "" : apartment.getHost().getLastName());
                        List<MenuItem> typeItems = FXCollections.observableArrayList(studioItem1, penthouseItem1, duplexItem1, triplexItem1);
                        selectedType(typeItems, typeMenu1);
                        List<MenuItem> stateItems = FXCollections.observableArrayList(availableItem1, occupiedItem1, reservedItem1, maintainingItem1);
                        selectedType(stateItems, stateMenu1);
                        updateResidentListsInApartment(apartment);
                        AtomicReference<Resident> selectedToBeHost = new AtomicReference<>(new Resident());
                        hostNameFilter1.getItems().clear();
                        if (apartment.getHost() == null){
                            for (Resident resident : apartment.getResidents()) {
                                MenuItem menuItem = new MenuItem(String.valueOf(resident.getLastName()));
                                menuItem.setId(String.valueOf(resident.getId()));
                                menuItem.setOnAction(event -> {
                                    selectedToBeHost.set(resident);
                                    hostNameFilter1.setText(menuItem.getText());
                                });
                                hostNameFilter1.getItems().add(menuItem);
                                menuItem.setStyle("-fx-border-radius: 14;" + "-fx-pref-width: 80px;");
                            }
                        }else {
                            selectedToBeHost.set(apartment.getHost());
                        }

                        editResBtn.setOnMouseClicked(e2 -> {
                            Apartment updateApartment = new Apartment(apartmentIDFilter1.getText(), apartment.getArea(), ApartmentType.valueOf(typeMenu1.getText()), ApartmentState.valueOf(stateMenu1.getText()), apartment.getRoomCount(), selectedToBeHost.get());
                            apartmentService.merge(updateApartment);
                            apartmentInfoDialog.setVisible(false);
                            dialogContainer.setVisible(false);
                            floorList.clear();
                            updateData();
                        });
                        addResBtn.setOnMouseClicked(mouseEvent -> MenuViewManager.switchViewToAddNewRes(MenuView.RESIDENT_FORM, selectedApartment, null));
                        AtomicReference<Resident> resident = new AtomicReference<>(new Resident());
                        residentTableView.setOnMouseClicked(mouseEvent -> {
                            resident.set(residentTableView.getSelectionModel().getSelectedItem());
                            if (mouseEvent.getClickCount() >= 2) {
                                Resident resident1 = residentTableView.getSelectionModel().getSelectedItem();
                                MenuViewManager.switchViewToShowResidentDetails(MenuView.RESIDENT_LIST, resident1);
                            }
                        });

                        delResBtn.setOnMouseClicked(e2 -> {
                            residentService.remove(resident.get());
                            updateData();
                            Apartment apartmentAfterUpdate = HibernateUtility.getSessionFactory().fromSession(session -> session.createQuery("from Apartment  where id = :id", Apartment.class)
                                    .setParameter("id", apartment.getId())
                                    .uniqueResult());
                            updateResidentListsInApartment(apartmentAfterUpdate);
                        });
                    });

                    HBox managingButton = new HBox(editBtn, deleteBtn);
                    managingButton.setStyle("-fx-alignment:center");
                    HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                    HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                    setGraphic(managingButton);

                }
                setText(null);
            }

        };
        actionsCol.setCellFactory(cellFactory);

        apartmentTableView.setItems(index);
    }
}

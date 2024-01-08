package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentCollectionRepository;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentCollectionService;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


//TODO: Warning when deleting apartment will also delete all residents in that apartment
public class ApartmentListController {
    public Label floorText;


    public Label tableHeader;
    public Button backButton;
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
    public MenuItem loftItem1;
    public MenuItem condoItem1;
    public MenuItem townhouseItem1;
    public MenuItem villaItem1;
    public MenuItem gardenItem1;


    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    private static final int TOTAL_FLOOR = 50;
    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    private final ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
    public Button addNewApartment;
    public TableColumn<ApartmentCollection, Double> amountCollectionCol;
    public TableColumn<ApartmentCollection, Integer> collectionIDCol;

    public TableView<ApartmentCollection> collectionTableView;
    public TableColumn<ApartmentCollection, Date> deadlinePaymentCollectionCol;
    public TableColumn<ApartmentCollection, String> nameCollectionCol;
    public TableColumn<ApartmentCollection, String> typeCollectionCol;
    public TableColumn<ApartmentCollection, String> isPaidCol;
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
        for (int i = 1; i <= TOTAL_FLOOR; i++) {
            ObservableMap<String, String> floorDetail = FXCollections.observableHashMap();
            int numberOfApartment;
            for (Apartment apartment : apartmentsToShowFloorList) {
                if (apartment.getFloor() == i) {
                    if (floorDetail.get("floor") == null) {
                        floorDetail.put("floor", String.valueOf(i));
                        floorDetail.put("totalApartments", "1");
                        floorDetail.put("totalResidents", String.valueOf(apartment.getResidents().size()));
                        updateAppsState(apartment, floorDetail);
                    } else {
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
            if (!floorDetail.isEmpty()) {
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

    public void showFloorList(List<Apartment> apartmentsToShowFloorList) {
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
        selectedItem(List.of(apartmentIDItem, hostNameItem, stateItem, typeItem, areaItem), apartmentMenuButton);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (apartmentMenuButton.visibleProperty().getValue()) {
                if (searchTextField.getText().isEmpty()) {
                    showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
                } else {
                    List<Apartment> filterList = new ArrayList<>();
                    List<Apartment> secondFilterList = new ArrayList<>();
                    List<Apartment> newApartmentList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                            .getResultList());
                    for (Apartment apartment : newApartmentList) {
                        if (apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))) {
                            filterList.add(apartment);
                        }
                    }
                    handleFilter(secondFilterList, newValue, filterList);
                    showApartments(FXCollections.observableList(secondFilterList));
                }
            } else {
                if (searchTextField.getText().isEmpty()) {
                    floorTableView.setItems(FXCollections.observableList(floorList));
                } else {
                    ObservableList<ObservableMap<String, String>> filterList = FXCollections.observableArrayList();
                    for (ObservableMap<String, String> floor : floorList) {
                        if (floor.get("floor").equals(searchTextField.getText())) {
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
            if (searchTextField.getText().isEmpty()) {
                showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
            } else {
                List<Apartment> newApartmentList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                        .getResultList());
                for (Apartment apartment : newApartmentList) {
                    if (apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))) {
                        filterList.add(apartment);
                    }
                }
                handleFilter(secondFilterList, searchTextField.getText(), filterList);
                showApartments(FXCollections.observableList(secondFilterList));
            }
        });
        floorTableView.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                searchTextField.setPromptText("Floor...");
                apartmentMenuButton.setVisible(false);
                searchTextField.setText("");
            } else {
                searchTextField.setPromptText("Search...");
                apartmentMenuButton.setVisible(true);
                searchTextField.setText("");

            }
        });
    }

    public void handleFilter(List<Apartment> filterList, String newValue, List<Apartment> apartments) {
        if (apartmentMenuButton.visibleProperty().get()) {
            switch (apartmentMenuButton.getText()) {
                case "ID Căn hộ":
                    for (Apartment apartment : apartments) {
                        if (apartment.getId().contains(newValue)) {
                            filterList.add(apartment);
                        }
                    }
                    break;
                case "Chủ căn hộ":
                    for (Apartment apartment : apartments) {
                        if (apartment.getHost() != null) {
                            if (apartment.getHost().getFirstName().contains(newValue)){
                                filterList.add(apartment);
                            }
                        }
                    }
                    break;
                case "Tình trạng":
                    for (Apartment apartment : apartments) {
                        switch (newValue){
                            case "Còn trống":
                                String available = "AVAILABLE";
                                if (String.valueOf(apartment.getState()).toLowerCase().contains(available.toLowerCase())){
                                    filterList.add(apartment);
                                }
                                break;
                            case "Đang sử dụng":
                                String occupied = "OCCUPIED";
                                if (String.valueOf(apartment.getState()).toLowerCase().contains(occupied.toLowerCase())){
                                    filterList.add(apartment);
                                }
                                break;
                            case "Đang sửa chữa":
                                String maintenance = "MAINTENANCE";
                                if (String.valueOf(apartment.getState()).toLowerCase().contains(maintenance.toLowerCase())){
                                    filterList.add(apartment);
                                }
                                break;
                            case "Đang bảo trì":
                                String reserved = "RESERVED";
                                if (String.valueOf(apartment.getState()).toLowerCase().contains(reserved.toLowerCase())){
                                    filterList.add(apartment);
                                }
                                break;
                        }
                    }
                    break;
                case "Loại căn hộ":
                    for (Apartment apartment : apartments) {
                        if (String.valueOf(apartment.getType()).toLowerCase().contains(newValue.toLowerCase())) {
                            filterList.add(apartment);
                        }
                    }
                    break;
                case "Diện tích":
                    for (Apartment apartment : apartments) {
                        if (apartment.getArea() == Double.parseDouble(newValue)) {
                            filterList.add(apartment);
                        }
                    }
                    break;
                default:
                    filterList.addAll(apartments);
                    break;
            }
        } else {
            for (Apartment apartment : apartments) {
                if (apartment.getId().contains(newValue)) {
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
        backButton.setOnMouseClicked(event -> {
            floorTableView.setVisible(true);
            apartmentTableView.setVisible(false);
            backButton.setVisible(false);
        });
    }

    public void handleAddNewApartment() {
        MenuViewManager.switchView(MenuView.APARTMENT_FORM);

    }

    public void showFloorDetail(int floor) {
        List<Apartment> floorApartments = new ArrayList<>();
        List<Apartment> apartmentsToShowFloorList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        for (Apartment apartment : apartmentsToShowFloorList) {
            if (apartment.getFloor() == floor) {
                floorApartments.add(apartment);
            }

        }
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(floorApartments);
        showApartments(apartmentObservableList);
    }

    public void handleClickedFloor() {
        floorTableView.setVisible(false);
        apartmentTableView.setVisible(true);
        backButton.setVisible(true);

    }

    public void toggleFilter() {
        if (closeDialogBtn.visibleProperty().getValue()) {
            closeDialogBtn.setOnMouseClicked(event -> {
                dialogContainer.setVisible(false);
                dialogBox.setVisible(false);
            });
        }
    }

    public void selectedItem(List<MenuItem> listItems, MenuButton typeMenu) {
        for (MenuItem selectedItems : listItems) {
            selectedItems.setOnAction(event -> typeMenu.setText(selectedItems.getText()));
        }
    }
    public void updateData() {
        List<Apartment> newApartmentList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
        floorList.clear();
        showFloorList(newApartmentList);
    }

    public void updateResidentListsInApartment(Apartment apartment) {
        ObservableList<Resident> residentObservableList = FXCollections.observableList(apartment.getResidents());
        residentID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getId())));
        phoneNumber.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        lastName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
        email.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        nationalID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNationalID()));
        residentTableView.setItems(residentObservableList);
    }

    public void updateCollectionListsInApartment(Apartment apartment) {
        List<ApartmentCollection> apartmentCollectionList = new ArrayList<>(apartment.getApartmentCollectionList());
        List<ApartmentCollection> monthFilterApartmentCollection = new ArrayList<>();
        for (ApartmentCollection apartmentCollection : apartmentCollectionList){
            if (apartmentCollection.getDeadlinePayment().getMonth() == Date.valueOf(LocalDate.now()).getMonth()){
                monthFilterApartmentCollection.add(apartmentCollection);
            }
        }
        collectionIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getId()));
        nameCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getName()));
        typeCollectionCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCollection().getType() == CollectionType.DONATION) {
                return new SimpleObjectProperty<>("Phí tình nguyện");
            } else if(cellData.getValue().getCollection().getType() == CollectionType.MANAGEMENT_FEE){
                return new SimpleObjectProperty<>("Phí quản lý");
            } else if (cellData.getValue().getCollection().getType() == CollectionType.SERVICE_FEE){
                return new SimpleObjectProperty<>("Phí dịch vụ");
            }
            return null;

        });
        amountCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getAmount()));
        isPaidCol.setCellValueFactory(cellData -> cellData.getValue().getState() == ApartmentCollectionState.PAID ? new SimpleObjectProperty<>("Đã trả") : new SimpleObjectProperty<>("Chưa trả"));
        isPaidCol.setCellFactory(column -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.equals("Đã trả")) {
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
            if (e.getClickCount() >= 2) {
                MenuViewManager.switchViewToShowCollectionDetail(MenuView.COLLECTION_REPORT, collectionTableView.getSelectionModel().getSelectedItem(), selectedApartment);
            }
        });
        collectionTableView.setItems(FXCollections.observableList(monthFilterApartmentCollection));
    }
    public void handleDeleteApartmentCollection(Apartment apartment){
        List<ApartmentCollection> apartmentCollectionList = apartmentCollectionService.findAll();
        for (ApartmentCollection apartmentCollection : apartmentCollectionList){
            if (apartmentCollection.getApartment().getId().equals(apartment.getId())){
                apartmentCollectionService.remove(apartmentCollection);
            }
        }
    }
    public void showApartments(ObservableList<Apartment> index) {
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        totalResidentsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidents().size()).asString());
        stateCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getState() == ApartmentState.AVAILABLE){
                return new SimpleObjectProperty<>("Còn trống");
            } else if(cellData.getValue().getState() == ApartmentState.MAINTENANCE){
                return new SimpleObjectProperty<>("Đang sửa chữa");
            } else if(cellData.getValue().getState() == ApartmentState.OCCUPIED){
                return new SimpleObjectProperty<>("Đang sử dụng");
            } else if(cellData.getValue().getState() == ApartmentState.RESERVED){
                return new SimpleObjectProperty<>("Đang bảo trì");
            }
            return null;
        });
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()).asString());
        hostNameCol.setCellValueFactory(cellData -> cellData.getValue().getHost() == null ? new SimpleObjectProperty<>("Unknown") : new SimpleObjectProperty<>(cellData.getValue().getHost().getLastName()));
        areaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        stateCol.setCellFactory(column -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.equals("Còn trống")) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("available-state");

                    } else if (item.equals("Đang sử dụng")) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("occupied-state");

                    } else if (item.equals("Đang sửa chữa")) {
                        getStyleClass().add("state-apartment-design");
                        getStyleClass().add("maintenance-state");

                    } else if (item.equals("Đang bảo trì")) {
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
                    deleteBtn.setOnMouseClicked(e -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Confirmation");
                        alert.setHeaderText("Are you sure you want to delete?");
                        alert.setContentText("Click OK to confirm, or Cancel to abort.");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                Apartment apartment = getTableView().getItems().get(getIndex());
                                handleDeleteApartmentCollection(apartment);
                                apartmentService.remove(apartment);
                                updateData();
                            }
                        });

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
                        if (apartment.getState() == ApartmentState.AVAILABLE){
                            stateMenu1.setText("Còn trống");
                        } else if(apartment.getState() == ApartmentState.MAINTENANCE){
                            stateMenu1.setText("Đang sửa chữa");
                        } else if(apartment.getState() == ApartmentState.OCCUPIED){
                            stateMenu1.setText("Đang sử dụng");
                        } else if(apartment.getState() == ApartmentState.RESERVED){
                            stateMenu1.setText("Đang bảo trì");
                        }
                        typeMenu1.setText(String.valueOf(apartment.getType()));
                        hostNameFilter1.setText(apartment.getHost() == null ? "" : apartment.getHost().getLastName());
                        List<MenuItem> typeItems = FXCollections.observableArrayList(studioItem1, penthouseItem1, duplexItem1, triplexItem1, gardenItem1, loftItem1, condoItem1, townhouseItem1, villaItem1);
                        selectedItem(typeItems, typeMenu1);
                        List<MenuItem> stateItems = FXCollections.observableArrayList(availableItem1, occupiedItem1, reservedItem1, maintainingItem1);
                        selectedItem(stateItems, stateMenu1);
                        updateResidentListsInApartment(apartment);
                        AtomicReference<Resident> selectedToBeHost = new AtomicReference<>(new Resident());
                        hostNameFilter1.getItems().clear();
                        for (Resident resident : apartment.getResidents()) {
                                MenuItem menuItem = new MenuItem(String.valueOf(resident.getLastName()));
                                hostNameFilter1.getItems().add(menuItem);
                                menuItem.setId(String.valueOf(resident.getId()));
                                menuItem.setOnAction(event -> {
                                    selectedToBeHost.set(resident);
                                    hostNameFilter1.setText(menuItem.getText());
                                });
                                menuItem.setStyle("-fx-border-radius: 14;" + "-fx-pref-width: 80px;");
                            }
                        selectedToBeHost.set(apartment.getHost());

                        editResBtn.setOnMouseClicked(e2 -> {
                            ApartmentState apartmentState = null;
                            if (stateMenu1.getText().equals("Đang sử dụng")){
                                apartmentState = ApartmentState.OCCUPIED;
                            } else if (stateMenu1.getText().equals("Đang bảo trì")){
                                apartmentState = ApartmentState.RESERVED;
                            } else if(stateMenu1.getText().equals("Còn trống")){
                                apartmentState = ApartmentState.AVAILABLE;
                            } else if (stateMenu1.getText().equals("Đang sửa chữa")){
                                apartmentState = ApartmentState.MAINTENANCE;
                            }
                            Apartment updateApartment = new Apartment(apartmentIDFilter1.getText(), apartment.getArea(), ApartmentType.valueOf(typeMenu1.getText()), apartmentState, apartment.getRoomCount(), selectedToBeHost.get());
                            apartmentService.merge(updateApartment);
                            apartmentInfoDialog.setVisible(false);
                            dialogContainer.setVisible(false);
                            floorList.clear();
                            updateData();
                        });
                        addResBtn.setOnMouseClicked(mouseEvent -> MenuViewManager.switchViewToAddNewRes(MenuView.RESIDENT_FORM, selectedApartment, selectedFloor));
                        AtomicReference<Resident> resident = new AtomicReference<>(new Resident());
                        residentTableView.setOnMouseClicked(mouseEvent -> {
                            resident.set(residentTableView.getSelectionModel().getSelectedItem());
                            if (mouseEvent.getClickCount() >= 2) {
                                Resident resident1 = residentTableView.getSelectionModel().getSelectedItem();
                                MenuViewManager.switchViewToShowResidentDetails(MenuView.RESIDENT_LIST, resident1, selectedFloor);
                            }
                        });

                        delResBtn.setOnMouseClicked(e2 -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Confirmation");
                            alert.setHeaderText("Are you sure you want to delete?");
                            alert.setContentText("Click OK to confirm, or Cancel to abort.");
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    residentService.remove(resident.get());
                                    if (selectedApartment.getResidents().isEmpty()){
                                        handleDeleteApartmentCollection(selectedApartment);
                                    }
                                    updateData();
                                    Apartment apartmentAfterUpdate = HibernateUtility.getSessionFactory().fromSession(session -> session.createQuery("from Apartment  where id = :id", Apartment.class)
                                            .setParameter("id", apartment.getId())
                                            .uniqueResult());
                                    updateResidentListsInApartment(apartmentAfterUpdate);
                                }
                            });

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

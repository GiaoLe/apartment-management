package com.example.demo.controller;
import com.example.demo.dao.*;
import com.example.demo.dao.Collection;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
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

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    public Button filterBtn;
    public TextField apartmentIDFilter;
    public TextField hostNameFilter;
    public MenuButton stateMenu;
    public MenuButton typeMenu;

    private final ObservableMap<String, String> tenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> eleventhFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> twelfthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> thirteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fourteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fifteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> sixteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> seventeenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> eighteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> nineteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> twentiethFloor = FXCollections.observableHashMap();
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
    List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
            .getResultList());
    private final int totalFloor = 50;
    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    public Button addNewApartment;
    public TableColumn<ApartmentCollection, Double> amountCollectionCol;
    public TableColumn<ApartmentCollection, Integer> collectionIDCol;

    public TableView<ApartmentCollection> collectionTableView;
    public TableColumn<ApartmentCollection, Date> deadlinePaymentCollectionCol;
    public TableColumn<ApartmentCollection, String> nameCollectionCol;
    public TableColumn<ApartmentCollection, CollectionType> typeCollectionCol;
    private final CollectionService collectionService = new CollectionService(new CollectionRepository());
    public TableColumn<ApartmentCollection, Boolean> isPaidCol;
    public Apartment selectedApartment;
    public void updateFloorDetails(List<Apartment> apartmentsToShowFloorList) {
        floorList = FXCollections.observableArrayList();
        for(int i = 1 ; i <= totalFloor ; i++){
            ObservableMap<String, String> floorDetail = FXCollections.observableHashMap();
            int numberOfApartment = 0;

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
                else {
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
    public void initialize() throws IOException {
        List<Apartment> apartmentsToShowFloorList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        showFloorList(apartmentsToShowFloorList);
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
            filterBtn.setVisible(false);
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
        filterBtn.setVisible(true);
    }
    public void toggleFilter(){
        if(filterBtn.visibleProperty().getValue()){
            filterBtn.setOnMouseClicked(event -> {
                dialogContainer.setVisible(true);
                dialogBox.setVisible(true);
            });
            List<MenuItem> typeItems = FXCollections.observableArrayList(studioItem, penthouseItem, duplexItem, triplexItem);
            List<MenuItem> stateItems = FXCollections.observableArrayList(availableItem, occupiedItem, reservedItem, maintainingItem);
            selectedType(typeItems, typeMenu);
            selectedState(stateItems, stateMenu);
            List<Apartment> selectedFloorForFilter = new ArrayList<>() ;
            List<Apartment> apartmentsToShowFloorList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                    .getResultList());
            for(Apartment apartment : apartmentsToShowFloorList){
                if(apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))){
                    selectedFloorForFilter.add(apartment);
                }
            }
            submitFilter.setOnMouseClicked(e -> {
                ApartmentFilter apartmentFilter = new ApartmentFilter(apartmentIDFilter, hostNameFilter, stateMenu, typeMenu, submitFilter, typeItems, stateItems, selectedFloorForFilter);
                List<Apartment> filteredApartment = apartmentFilter.handleFilter();
                if (filteredApartment.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Không tìm thấy kết quả");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy căn hộ " +apartmentIDFilter.getText());
                    alert.showAndWait();
                }
                else {
                    dialogContainer.setVisible(false);
                    dialogBox.setVisible(false);
                    apartmentIDFilter.clear();
                    hostNameFilter.clear();
                    stateMenu.setText("Choose State");
                    typeMenu.setText("Choose Type");
                    ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(filteredApartment);
                    showApartments(apartmentObservableList);
                }
            });
        }
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
    public void selectedState(List<MenuItem> listItems, MenuButton stateMenu){
        for (MenuItem selectedItems : listItems){
            selectedItems.setOnAction(event -> stateMenu.setText(selectedItems.getText()));
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
        deadlinePaymentCollectionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCollection().getTimeToPay()));
        isPaidCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isPaid()));
        isPaidCol.setCellFactory(column -> {
            return new TextFieldTableCell<ApartmentCollection, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null){
                        if(item){
                            getStyleClass().add("paid");
                            getStyleClass().add("state-apartment-design");

                        }else{
                            getStyleClass().add("notPaid");
                            getStyleClass().add("state-apartment-design");

                        }
                    }
                }
            };
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
        areaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        stateCol.setCellFactory(column -> {
            return new TextFieldTableCell<Apartment, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null){
                        if(item.equals(ApartmentState.AVAILABLE.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("available-state");

                        }else if(item.equals(ApartmentState.OCCUPIED.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("occupied-state");

                        }else if(item.equals(ApartmentState.MAINTENANCE.toString())) {
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("maintenance-state");

                        }else if(item.equals(ApartmentState.RESERVED.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("reserved-state");


                        }
                    }
                }
            };
        });
        Callback<TableColumn<Apartment, String>, TableCell<Apartment, String>> cellFoctory = (TableColumn<Apartment, String> param) -> {
            final TableCell<Apartment, String> cell = new TableCell<Apartment, String>() {
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

                        // Lấy đường dẫn đầy đủ của tệp ảnh từ thư mục resources/images
                        String imagePath = getClass().getResource("/images/pencil.png").toExternalForm();
                        String imagePath1 = getClass().getResource("/images/delete.png").toExternalForm();

                        // Tạo một đối tượng Image từ đường dẫn
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
                        if(rowIndex % 2 == 0){
                            editBtn.setStyle("-fx-background-color: #fff;" + "-fx-cursor:HAND;" );
                            deleteBtn.setStyle("-fx-background-color: #fff;" + "-fx-cursor:HAND;" );
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
                            editBtn.setStyle("-fx-background-color: #f0f0f0;" + "-fx-cursor:HAND;" );
                            deleteBtn.setStyle("-fx-background-color: #f0f0f0;" + "-fx-cursor:HAND;" );
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
                            List<MenuItem> typeItems = FXCollections.observableArrayList(studioItem1, penthouseItem1, duplexItem1, triplexItem1);
                            selectedType(typeItems, typeMenu1);
                            List<MenuItem> stateItems = FXCollections.observableArrayList(availableItem1, occupiedItem1, reservedItem1, maintainingItem1);
                            selectedType(stateItems, stateMenu1);
                            updateResidentListsInApartment(apartment);
                            editResBtn.setOnMouseClicked(e2 -> {
                                Apartment updateApartment = new Apartment(apartmentIDFilter1.getText(), apartment.getArea(), ApartmentType.valueOf(typeMenu1.getText()), ApartmentState.valueOf(stateMenu1.getText()), apartment.getRoomCount());
                                apartmentService.merge(updateApartment);
                                apartmentInfoDialog.setVisible(false);
                                dialogContainer.setVisible(false);
                                floorList.clear();
                                updateData();
                            });
                            hostNameFilter1.getItems().clear();
                            for(Resident resident : apartment.getResidents()){
                                MenuItem menuItem = new MenuItem(String.valueOf(resident.getId()));
                                menuItem.setId(String.valueOf(resident.getId()));
                                menuItem.setOnAction(event -> {
                                    hostNameFilter1.setText(menuItem.getText());
                                });
                                hostNameFilter1.getItems().add(menuItem);
                                menuItem.setStyle("-fx-border-radius: 14;" + "-fx-pref-width: 80px;");

                            }
                            addResBtn.setOnMouseClicked(e2 -> {
                                MenuViewManager.switchViewToAddNewRes(MenuView.RESIDENT_FORM, selectedApartment);
                            });
                            AtomicReference<Resident> resident = new AtomicReference<>(new Resident());
                            residentTableView.setOnMouseClicked(e2 -> {
                                resident.set(residentTableView.getSelectionModel().getSelectedItem());
                                if(e2.getClickCount() >= 2){
                                    Resident resident1 = residentTableView.getSelectionModel().getSelectedItem();
                                    MenuViewManager.switchViewToShowResidentDetails(MenuView.RESIDENT_LIST, resident1);
                                }
                            });

                            delResBtn.setOnMouseClicked(e2 -> {
                                residentService.remove(resident.get());
                                updateData();
                                Apartment apartmentAftetUpdate = HibernateUtility.getSessionFactory().fromSession(session -> session.createQuery("from Apartment  where id = :id", Apartment.class)
                                        .setParameter("id", apartment.getId())
                                        .uniqueResult());
                                updateResidentListsInApartment(apartmentAftetUpdate);
                            });
                        });

                        HBox managebtn = new HBox(editBtn, deleteBtn);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };

            return cell;
        };
        actionsCol.setCellFactory(cellFoctory);

        apartmentTableView.setItems(index);
    }
}

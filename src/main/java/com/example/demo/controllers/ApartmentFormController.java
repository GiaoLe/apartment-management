package com.example.demo.controllers;

import com.example.demo.dao.*;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repositories.*;
import com.example.demo.services.ApartmentCollectionService;
import com.example.demo.services.ApartmentService;
import com.example.demo.services.CollectionService;
import com.example.demo.services.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApartmentFormController {
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    public TextField idTextField;
    public TextField areaTextField;
    public TextField roomCountTextField;
    public ChoiceBox<ApartmentType> apartmentTypeChoiceBox;
    public TableColumn<ObservableMap<String, String>, String> firstNameCol;
    public TableColumn<ObservableMap<String, String>, String> lastNameCol;
    public TableColumn<ObservableMap<String, String>, String> nationalIDCol;
    public TableColumn<ObservableMap<String, String>, String> phoneNumberCol;
    public TableColumn<ObservableMap<String, String>, String> resIDCol;
    public TableView<ObservableMap<String, String>> residentTableView;
    public Button addNewResBtn;
    private final ObservableList<ObservableMap<String, String>> residentList = FXCollections.observableArrayList();

    public AnchorPane addNewResContainer;
    public AnchorPane dialogContainer;
    public Button addNewRedCloseBtn;
    public TextField apartmentTextField;
    public Label emailErrorLabel;

    public TextField emailTextField;
    public Label firstNameErrorLabel;

    public TextField firstNameTextField;
    public Label lastNameErrorLabel;

    public TextField lastNameTextField;
    public Label nationalIDErrorLabel;

    public TextField nationalIDTextField;
    public Label phoneNumberErrorLabel;

    public TextField phoneNumberTextField;

    public TextField residentIDTextField;
    public MenuItem maleItem;
    public MenuItem femaleItem;
    public MenuButton genderMenuButton;
    public Button residentSubmissionButton;
    public Button apartmentSubmissionButton;
    public Label apartmentTextFieldErrorLabel;
    ResidentService residentService = new ResidentService(new ResidentRepository());
    public TableColumn<Apartment, String> apartmentIdCol;
    public TableColumn<Apartment, String> stateCol;
    public TableColumn<Apartment, String> totalResidentsCol;
    public TableColumn<Apartment, ApartmentType> typeCol;

    public TableView<Apartment> apartmentTableView;
    public TextField IDTextField;
    public DatePicker datePicker;
    public DatePicker dobPicker;
    private final CollectionService collectionService = new CollectionService(new CollectionRepository());

    @FXML
    public void initialize() {
        apartmentTypeChoiceBox.getItems().addAll(ApartmentType.values());
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isTextFieldEmpty = newValue.trim().isEmpty();
            addNewResBtn.setDisable(isTextFieldEmpty);
        });
        updateApartmentTableView();
        selectedGender(List.of(maleItem, femaleItem));
    }

    public void selectedGender(List<MenuItem> list) {
        for (MenuItem menuItem : list) {
            menuItem.setOnAction(e -> genderMenuButton.setText(menuItem.getText()));
        }
    }

    public void submitButtonOnAction() {
        ApartmentState apartmentStateToSet;
        if (residentList.isEmpty()) {
            apartmentStateToSet = ApartmentState.AVAILABLE;
        } else {
            apartmentStateToSet = ApartmentState.OCCUPIED;
            List<Collection> collections = collectionService.findAll();
            for (Collection collection : collections) {
                if (collection.getType() == CollectionType.SERVICE_FEE || collection.getType() == CollectionType.MANAGEMENT_FEE) {
                    ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
                    List<Apartment> apartments = apartmentService.findAll();
                    LocalDate localDate = LocalDate.now();
                    localDate = localDate.plusDays(30);
                    for (Apartment apartment : apartments) {
                        apartmentCollectionService.merge(new ApartmentCollection(apartment, collection, Date.valueOf(localDate)));
                    }
                }
            }
        }
        List<Resident> residentList = new ArrayList<>();
        Apartment apartment = Apartment.builder()
                .id(idTextField.getText())
                .area(Double.parseDouble(areaTextField.getText()))
                .roomCount(Integer.parseInt(roomCountTextField.getText()))
                .type(apartmentTypeChoiceBox.getValue())
                .state(apartmentStateToSet)
                .build();
        apartmentService.persist(apartment);
        for (ObservableMap<String, String> observableMap : this.residentList) {
            Resident resident = new Resident(
                    observableMap.get("firstName"), observableMap.get("lastName"), Gender.MALE, Date.valueOf(observableMap.get("dob")),
                    observableMap.get("nationalID"), observableMap.get("phoneNumber"), observableMap.get("email"), apartment,
                    Date.valueOf(observableMap.get("datePicker"))
            );
            residentList.add(resident);
            residentService.persist(resident);
        }
        apartment.setResidents(residentList);
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }

    public void addNewResidentButtonOnAction() {
        dialogContainer.setVisible(true);
        addNewResContainer.setVisible(true);
        apartmentTextField.setText(idTextField.getText());
        apartmentTextField.setDisable(true);
    }

    public void updateApartmentTableView() {
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        totalResidentsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidents().size()).asString());
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        stateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()).asString());
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
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(apartments);
        apartmentTableView.setItems(apartmentObservableList);
    }

    public void handleSubmitAddRes() {
        ObservableMap<String, String> residents = FXCollections.observableHashMap();
        residents.put("ID", IDTextField.getText());
        residents.put("gender", genderMenuButton.getText());
        residents.put("dob", Date.valueOf(dobPicker.getValue()).toString());
        residents.put("firstName", firstNameTextField.getText());
        residents.put("lastName", lastNameTextField.getText());
        residents.put("nationalID", nationalIDTextField.getText());
        residents.put("phoneNumber", phoneNumberTextField.getText());
        residents.put("email", emailTextField.getText());
        residents.put("datePicker", Date.valueOf(datePicker.getValue()).toString());
        residentTableView.setVisible(true);
        residentList.add(residents);
        resIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("ID")));
        firstNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("firstName")));
        lastNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("lastName")));
        phoneNumberCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("phoneNumber")));
        nationalIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("nationalID")));
        residentTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                dialogContainer.setVisible(true);
                addNewResContainer.setVisible(true);
                ObservableMap<String, String> selectedResident = residentTableView.getSelectionModel().getSelectedItem();
                residentIDTextField.setText(selectedResident.get("ID"));
                dobPicker.setValue(Date.valueOf(selectedResident.get("dob")).toLocalDate());
                datePicker.setValue(LocalDate.parse(selectedResident.get("datePicker")));
                firstNameTextField.setText(selectedResident.get("firstName"));
                lastNameTextField.setText(selectedResident.get("lastName"));
                apartmentTextField.setText(idTextField.getText());
                nationalIDTextField.setText(selectedResident.get("nationalID"));
                phoneNumberTextField.setText(selectedResident.get("phoneNumber"));
                emailTextField.setText(selectedResident.get("email"));
            }
        });
        residentTableView.setItems(residentList);
        dialogContainer.setVisible(false);
        addNewResContainer.setVisible(false);
        clearTextField(new ArrayList<>(List.of(residentIDTextField, lastNameTextField, firstNameTextField, phoneNumberTextField, nationalIDTextField, emailTextField)));
    }

    public void handleCloseAddNewRes() {
        dialogContainer.setVisible(false);
        addNewResContainer.setVisible(false);
    }

    public void clearTextField(List<TextField> listTextFields) {
        for (TextField textField : listTextFields) {
            textField.setText("");
        }
    }
}

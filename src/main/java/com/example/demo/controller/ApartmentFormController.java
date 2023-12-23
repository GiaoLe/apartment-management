package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentState;
import com.example.demo.dao.ApartmentType;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.HibernateUtility;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApartmentFormController {
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    public TextField idTextField;
    public TextField areaTextField;
    public Button submitButton;
    public TextField roomCountTextField;
    public ChoiceBox<ApartmentType> apartmentTypeChoiceBox;
    public TableColumn<ObservableMap<String, String>, String> firstNameCol;
    public TableColumn<ObservableMap<String, String>, String> lastNameCol;
    public TableColumn<ObservableMap<String, String>, String> nationalIDCol;
    public TableColumn<ObservableMap<String, String>, String> phoneNumberCol;
    public TableColumn<ObservableMap<String, String>, String> resIDCol;
    public TableView<ObservableMap<String, String>> residentTableView;
    public Button addNewResBtn;
    private ObservableMap<String, String> residents;
    private final ObservableList<ObservableMap<String, String>> residentlist = FXCollections.observableArrayList();

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
    ResidentService residentService = new ResidentService(new ResidentRepository());
    public TableColumn<Apartment, String> apartmentIdCol;
    public TableColumn<Apartment, String> stateCol;
    public TableColumn<Apartment, String> totalResidentsCol;
    public TableColumn<Apartment, ApartmentType> typeCol;

    public TableView<Apartment> apartmentTableView;
    public TextField IDTextField;
    public DatePicker datePicker;
    @FXML
    public void initialize() {

        apartmentTypeChoiceBox.getItems().addAll(ApartmentType.values());
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isTextFieldEmpty = newValue.trim().isEmpty();
            addNewResBtn.setDisable(isTextFieldEmpty);
        });
        updateApartmentTableView();
    }

    public void submitButtonOnAction() {
        List<Resident> residentList = new ArrayList<>();
        Apartment apartment = Apartment.builder()
                .id(idTextField.getText())
                .area(Double.parseDouble(areaTextField.getText()))
                .roomCount(Integer.parseInt(roomCountTextField.getText()))
                .type(apartmentTypeChoiceBox.getValue())
                .state(ApartmentState.AVAILABLE)
                .build();
        apartmentService.persist(apartment);
        for(ObservableMap<String, String> observableMap : residentlist){
            Resident resident = new Resident(
                    observableMap.get("ID"),
                    observableMap.get("firstName"),
                    observableMap.get("lastName"),
                    apartment,
                    observableMap.get("phoneNumber"),
                    observableMap.get("email"),
                    observableMap.get("nationalID"),
                    Date.valueOf(observableMap.get("datePicker"))
            );
            residentList.add(resident);
            residentService.persist(resident);
        }
        apartment.setResidents(residentList);
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }
    public void handleAddNewRes() {
        dialogContainer.setVisible(true);
        addNewResContainer.setVisible(true);
        apartmentTextField.setText(idTextField.getText());
        apartmentTextField.setDisable(true);
    }
    public void updateApartmentTableView () {
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        totalResidentsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidents().size()).asString());
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        stateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()).asString());
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
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(apartments);
        apartmentTableView.setItems(apartmentObservableList);
    }
    public void handleSubmitAddRes() {
        residents = FXCollections.observableHashMap();
        residents.put("ID", IDTextField.getText());
        residents.put("firstName", firstNameTextField.getText());
        residents.put("lastName", lastNameTextField.getText());
        residents.put("nationalID", nationalIDTextField.getText());
        residents.put("phoneNumber", phoneNumberTextField.getText());
        residents.put("email", emailTextField.getText());
        residents.put("datePicker", Date.valueOf(datePicker.getValue()).toString());
        residentTableView.setVisible(true);
        residentlist.add(residents);
        resIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("ID")));
        firstNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("firstName")));
        lastNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("lastName")));
        phoneNumberCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("phoneNumber")));
        nationalIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("nationalID")));
        residentTableView.setOnMouseClicked(event -> {
            if(event.getClickCount() >= 2){
                dialogContainer.setVisible(true);
                addNewResContainer.setVisible(true);
                ObservableMap<String, String> selectedResident = residentTableView.getSelectionModel().getSelectedItem();
                residentIDTextField.setText(selectedResident.get("ID"));
                firstNameTextField.setText(selectedResident.get("firstName"));
                lastNameTextField.setText(selectedResident.get("lastName"));
                apartmentTextField.setText(idTextField.getText());
                nationalIDTextField.setText(selectedResident.get("nationalID"));
                phoneNumberTextField.setText(selectedResident.get("phoneNumber"));
                emailTextField.setText(selectedResident.get("email"));
            }
        });
        residentTableView.setItems(residentlist);
        dialogContainer.setVisible(false);
        addNewResContainer.setVisible(false);
        clearTextField(new ArrayList<>(List.of(residentIDTextField, lastNameTextField, firstNameTextField, phoneNumberTextField, nationalIDTextField, emailTextField)));
    }

    public void handleCloseAddNewRes(){
        dialogContainer.setVisible(false);
        addNewResContainer.setVisible(false);
    }
    public void clearTextField(List<TextField> listTextFields){
        for (TextField textField : listTextFields){
            textField.setText("");
        }
    }
}

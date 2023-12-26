package com.example.demo.controller;

import com.example.demo.repository.HibernateUtility;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.dao.Apartment;
import com.example.demo.dao.Resident;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ResidentFormController {

    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField apartmentTextField;
    public Button submitButton;
    public Label apartmentTextFieldErrorLabel;
    public Label firstNameErrorLabel;
    public Label lastNameErrorLabel;
    public Button backButton;
    public TextField nationalIDTextField;
    public Label nationalIDErrorLabel;
    public TextField phoneNumberTextField;
    public Label phoneNumberErrorLabel;
    public TextField emailTextField;
    public Label emailErrorLabel;

    private ArrayList<TextFieldWrapper> textFieldWrappers;

    private ResidentService residentService;
    public DatePicker datePicker;
    public TextField IDTextField;
    public MenuItem maleItem;
    public MenuItem femaleItem;
    public MenuButton genderMenuButton;
    public Boolean switchViewFlag = false;
    public TableColumn<Resident, String> apartmentCol;
    public TableColumn<Resident, String>  firstNameCol;
    public TableColumn<Resident, Boolean>  genderCol;
    public TableColumn<Resident, String>  lastNameCol;
    public TableColumn<Resident, String>  phoneNumberCol;
    public TableColumn<Resident, Integer>  resIDCol;
    public TableView<Resident> residentTableView;
    public DatePicker dobPicker;
    public ObservableMap<String, String> selectedFloor = FXCollections.observableHashMap();

    @FXML
    public void initialize() {
        textFieldWrappers = new ArrayList<>(List.of(
                new TextFieldWrapper(firstNameTextField, firstNameErrorLabel),
                new TextFieldWrapper(lastNameTextField, lastNameErrorLabel),
                new TextFieldWrapper(apartmentTextField, apartmentTextFieldErrorLabel),
                new TextFieldWrapper(nationalIDTextField, nationalIDErrorLabel),
                new TextFieldWrapper(phoneNumberTextField, phoneNumberErrorLabel),
                new TextFieldWrapper(emailTextField, emailErrorLabel)
        ));
        residentService = new ResidentService(new ResidentRepository());
        updateResidentTableView();
        selectedGender(List.of(maleItem, femaleItem));
    }

    public void submitButtonOnAction() {
        for (TextFieldWrapper textFieldWrapper : textFieldWrappers) {
            textFieldWrapper.checkEmpty();
        }
        boolean allFieldsAreFilled = textFieldWrappers.stream().noneMatch(TextFieldWrapper::isEmpty);
        if (allFieldsAreFilled) {
            persistResident();
        }
        updateResidentTableView();
    }
    public void updateResidentTableView(){
        List<Resident> residents = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Resident order by apartment.id", Resident.class)
                .getResultList());
        residentTableView.setItems(FXCollections.observableList(residents));
        resIDCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        firstNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
        phoneNumberCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
        genderCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getGender()));
        genderCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean isMale, boolean empty) {
                super.updateItem(isMale, empty);
                if (empty || isMale == null) {
                    setText(null);
                } else {
                    setText(isMale ? "Female" : "Male");
                }
            }
        });
        apartmentCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartmentID()));
    }
    public void selectedGender(List<MenuItem> list){
        for (MenuItem menuItem : list){
            menuItem.setOnAction(e -> {
                genderMenuButton.setText(menuItem.getText());
            });
        }
    }
    private void persistResident() {
        Apartment apartment = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment where id = :id", Apartment.class)
                .setParameter("id", apartmentTextField.getText())
                .uniqueResult());
        if (apartment == null) {
            //TODO retain resident information after apartment creation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Apartment with id " + apartmentTextField.getText() + " does not exist. Please create it first.");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> MenuViewManager.switchView(MenuView.APARTMENT_FORM));
        } else {
            Resident resident = new Resident(
                    IDTextField.getText(),
                    Date.valueOf(dobPicker.getValue()),
                    genderMenuButton.getText(),
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    apartment,
                    phoneNumberTextField.getText(),
                    emailTextField.getText(),
                    nationalIDTextField.getText(),
                    Date.valueOf(datePicker.getValue())
            );
            residentService.persist(resident);
            apartment.addResident(resident);
            if(switchViewFlag){
                MenuViewManager.switchViewFromResidentListToShowApartmentDetail(MenuView.APARTMENT_LIST, resident, selectedFloor);
            }else {
                MenuViewManager.switchView(MenuView.RESIDENT_LIST);
            }
        }
    }

    public void backButtonOnAction() {
        MenuViewManager.switchView(MenuView.RESIDENT_LIST);
    }
}


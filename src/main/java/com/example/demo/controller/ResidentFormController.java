package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.dao.Collection;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.*;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.UnaryOperator;

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
    private CollectionService collectionService = new CollectionService(new CollectionRepository());
    private ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
    public Label idErrorLabel;
    public Label dobErrorLabel;
    public Label moveInDateErrorLabel;
    public Label apartmentErrorLabel;
    public MenuButton apartmentMenuButton;
    public Label genderErrorLabel;
    private Boolean invalidFlag;
    @FXML
    public void initialize() {
        residentService = new ResidentService(new ResidentRepository());
        initializeResidentTableView();
        selectedGender(List.of(maleItem, femaleItem));
        setFormatter();
        setApartmentForMenuButton();
    }

    public void submitButtonOnAction() {
        checkInvalidDate();
        checkInvalidInput();
        List<Label> errorList = new ArrayList<>(List.of(idErrorLabel, firstNameErrorLabel, lastNameErrorLabel, apartmentErrorLabel, dobErrorLabel, moveInDateErrorLabel, phoneNumberErrorLabel, emailErrorLabel, genderErrorLabel));
        for (Label label : errorList){
            if (Objects.equals(label.getText(), "")){
                invalidFlag = false;
            } else {
                invalidFlag = true;
            }
        }
        if(!invalidFlag){
            persistResident();

        }
        residentTableView.refresh();
    }
    public void setApartmentForMenuButton(){
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id", Apartment.class)
                .getResultList());
        for (Apartment apartment : apartments){
            MenuItem menuItem = new MenuItem(apartment.getId().toString());
            apartmentMenuButton.getItems().add(menuItem);
        }
        List<MenuItem> menuButtonItem = apartmentMenuButton.getItems();
        for (MenuItem menuItem : menuButtonItem){
            menuItem.setOnAction(e -> {
                apartmentMenuButton.setText(menuItem.getText());
            });
        }
    }
    public void setFormatter(){
        UnaryOperator<TextFormatter.Change> phoneNumberFilter = change -> {
            if (change.isDeleted() && change.getControlText().length() == 1) {
                return change;
            }
            if (change.getControlNewText().length() > 10) {
                return null;
            }
            if (change.getControlNewText().matches("[0-9]+")) {
                return change;
            } else {
                return null;
            }

        };
        UnaryOperator<TextFormatter.Change> IDNumberFilter = change -> {
            if (change.isDeleted() && change.getControlText().length() == 1) {
                return change;
            }
            if (change.getControlNewText().length() > 12) {
                return null;
            }
            if (change.getControlNewText().matches("[0-9]+")) {
                return change;
            } else {
                return null;
            }
        };

        StringConverter<String> stringConverter = new StringConverter<String>() {
            @Override
            public String fromString(String string) {
                return string;
            }

            @Override
            public String toString(String object) {
                return object;
            }
        };

        TextFormatter<String> phoneNumberFormatter = new TextFormatter<>(stringConverter, "", phoneNumberFilter);
        TextFormatter<String> IDNumberFormatter = new TextFormatter<>(stringConverter, "", IDNumberFilter);

        phoneNumberTextField.setTextFormatter(phoneNumberFormatter);
        IDTextField.setTextFormatter(IDNumberFormatter);
        UnaryOperator<TextFormatter.Change> stringFilter = change -> {
            if (change.isDeleted() && change.getControlText().length() == 1) {
                return change;
            }
            if (change.getControlNewText().matches("[a-zA-Z\\p{IsAlphabetic} ]*")) {
                return change;
            } else {
                return null;
            }
        };
        StringConverter<String> stringConverter1 = new DefaultStringConverter();

        TextFormatter<String> firstNameFormatter = new TextFormatter<>(stringConverter1, "", stringFilter);
        TextFormatter<String> lastNameFormatter = new TextFormatter<>(stringConverter1, "", stringFilter);
        TextFormatter<String> nationalIDFormatter = new TextFormatter<>(stringConverter1, "", stringFilter);
        firstNameTextField.setTextFormatter(firstNameFormatter);
        lastNameTextField.setTextFormatter(lastNameFormatter);
        nationalIDTextField.setTextFormatter(nationalIDFormatter);
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return LocalDate.parse(string, dateFormatter);
                } catch (Exception e) {
                    return null;
                }
            }
        };
        dobPicker.setConverter(converter);

    }
    public void checkInvalidDate () {
        ArrayList<DatePicker> datePickers = new ArrayList<>(List.of(dobPicker, datePicker));

        for (DatePicker datePicker1 : datePickers){
            LocalDate localDate = datePicker1.getValue();
            if(datePicker1 == dobPicker){
                if (localDate != null){
                    dobErrorLabel.setText("");
                } else {
                    dobErrorLabel.setText("Ngày không hợp lệ");
                    dobErrorLabel.setTextFill(Color.RED);
                }
            } else {
                if (localDate != null){
                    moveInDateErrorLabel.setText("");
                } else {
                    moveInDateErrorLabel.setText("Ngày không hợp lệ");
                    moveInDateErrorLabel.setTextFill(Color.RED);
                }
            }
        }

    }
    public void checkInvalidInput(){
        List<TextField> textFields = new ArrayList<>(List.of(IDTextField, firstNameTextField, lastNameTextField,  phoneNumberTextField, nationalIDTextField, emailTextField));
        for (TextField textField : textFields){
            if(textField == IDTextField){
                if (textField.getText().isEmpty()){
                    idErrorLabel.setText("Yêu cầu nhập thông tin");
                    idErrorLabel.setTextFill(Color.RED);
                } else {
                    if (textField.getText().length() != 12){
                        idErrorLabel.setText("Số CCCD phải đầy đủ 12 số");
                        idErrorLabel.setTextFill(Color.RED);
                    } else {
                       idErrorLabel.setText("");
                    }
                }
            } else if(textField == firstNameTextField){
                if(textField.getText().isEmpty()){
                    firstNameErrorLabel.setText("Yêu cầu nhập thông tin");
                    firstNameErrorLabel.setTextFill(Color.RED);
                }
                else {
                    firstNameErrorLabel.setText("");

                }
            } else if(textField == lastNameTextField){
                if(textField.getText().isEmpty()){
                    lastNameErrorLabel.setText("Yêu cầu nhập thông tin");
                    lastNameErrorLabel.setTextFill(Color.RED);
                }else {
                    lastNameErrorLabel.setText("");

                }
            } else if(textField == nationalIDTextField){
                if (textField.getText().isEmpty()){
                    nationalIDErrorLabel.setText("Yêu cầu nhập thông tin");
                    nationalIDErrorLabel.setTextFill(Color.RED);
                }else {
                    nationalIDErrorLabel.setText("");

                }
            } else if(textField == phoneNumberTextField){
                if (phoneNumberTextField.getText().isEmpty()){
                    phoneNumberErrorLabel.setText("Yêu cầu nhập thông tin");
                    phoneNumberErrorLabel.setTextFill(Color.RED);
                } else {
                    phoneNumberErrorLabel.setText("");

                }
            }
        }
        List<MenuButton> menuButtons = new ArrayList<>(List.of(genderMenuButton, apartmentMenuButton));
        for (MenuButton menuButton : menuButtons){
            if (menuButton == genderMenuButton){
                if (Objects.equals(menuButton.getText(), "Giới tính")){
                    genderErrorLabel.setTextFill(Color.RED);
                    genderErrorLabel.setText("Giới tính không hợp lệ");
                    genderErrorLabel.setText("");

                }
            } else {
                if(Objects.equals(menuButton.getText(), "Chọn căn hộ")){
                    apartmentErrorLabel.setText("Căn hộ không hợp lệ");
                    apartmentErrorLabel.setTextFill(Color.RED);
                }
                else {
                    apartmentErrorLabel.setText("");
                }
            }
        }
    }
    public void initializeResidentTableView(){
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
                    setText(isMale ? "Nữ" : "Nam");
                }
            }
        });
        apartmentCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApartmentID()));
    }
    public void selectedGender(List<MenuItem> list){
        for (MenuItem menuItem : list){
            menuItem.setOnAction(e -> genderMenuButton.setText(menuItem.getText()));
        }
    }
    private void persistResident() {
        Apartment apartment = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment where id = :id", Apartment.class)
                .setParameter("id", apartmentMenuButton.getText())
                .uniqueResult());
        if (apartment == null) {
            //TODO retain resident information after apartment creation
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setContentText("Apartment with id " + apartmentTextField.getText() + " does not exist. Please create it first.");
//            alert.showAndWait()
//                    .filter(response -> response == ButtonType.OK)
//                    .ifPresent(response -> MenuViewManager.switchView(MenuView.APARTMENT_FORM));
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
            if (apartment.getResidents().isEmpty()){
                List<Collection> collections = collectionService.findAll();
                Date moveInDate = Date.valueOf(datePicker.getValue());
                if (moveInDate.getYear() + 1900 < LocalDate.now().getYear()){
                    LocalDate localDate = LocalDate.of(moveInDate.getYear() + 1900, moveInDate.getMonth() + 1, 30);
                    while(localDate.isBefore(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().plus(1), LocalDate.now().getDayOfMonth()))){
                        if ((30 - moveInDate.getDate()) > 5){
                            for (Collection collection : collections){
                                if (collection.getType() == CollectionType.MANAGEMENT_FEE || collection.getType() == CollectionType.SERVICE_FEE){
                                    apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, Date.valueOf(localDate)));
                                }
                            }
                        }
                        localDate=localDate.plusMonths(1);
                    }
                }else {
                    for (Collection collection : collections){
                        for (int i = moveInDate.getMonth() ; i < LocalDate.now().getMonthValue() ; i++) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, LocalDate.now().getYear());
                            calendar.set(Calendar.DAY_OF_MONTH, 30);
                            if (moveInDate.getMonth() == apartment.getHost().getMoveInDate().getMonth()){
                                calendar.set(Calendar.MONTH, i);
                                java.util.Date date = calendar.getTime();
                                if (30 - apartment.getHost().getMoveInDate().getDate() > 5){
                                    apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date));
                                }
                            }else {
                                calendar.set(Calendar.MONTH, i);
                                java.util.Date date = calendar.getTime();
                                apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date));
                            }
                        }
                    }
                }
            }
            residentService.persist(resident);
            apartment.addResident(resident);
            apartment.setState(ApartmentState.OCCUPIED);
            ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
            apartmentService.merge(apartment);

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


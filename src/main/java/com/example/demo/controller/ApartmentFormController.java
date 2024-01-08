package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.*;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.hibernate.Session;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

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
    public Button backButton;
    public Button apartmentSubmissionButton;
    public Label idErrorLabel;
    public Label dobErrorLabel;
    public Label moveInDateErrorLabel;
    public Label apartmentErrorLabel;
    ResidentService residentService = new ResidentService(new ResidentRepository());
    public TableColumn<Apartment, String> apartmentIdCol;
    public TableColumn<Apartment, String> stateCol;
    public TableColumn<Apartment, String> totalResidentsCol;
    public TableColumn<Apartment, ApartmentType> typeCol;

    public TableView<Apartment> apartmentTableView;
    public TextField IDTextField;
    public DatePicker datePicker;
    public DatePicker dobPicker;
    public Label genderErrorLabel;
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
        setFormatter();
    }

    public void selectedGender(List<MenuItem> list) {
        for (MenuItem menuItem : list) {
            menuItem.setOnAction(e -> genderMenuButton.setText(menuItem.getText()));
        }
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
                        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
                            Resident resident = session.createQuery("from Resident where IDNumber = :id", Resident.class)
                                    .setParameter("id", IDTextField.getText())
                                    .uniqueResult();

                            if (resident != null) {
                                idErrorLabel.setText("Số CCCD đã tồn tại");
                                idErrorLabel.setTextFill(Color.RED);
                            } else {
                                idErrorLabel.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    System.out.println("national empty");
                    nationalIDErrorLabel.setText("Yêu cầu nhập thông tin");
                    nationalIDErrorLabel.setTextFill(Color.RED);
                }else {
                    nationalIDErrorLabel.setText("");

                }
            } else if(textField == phoneNumberTextField){
                if (phoneNumberTextField.getText().isEmpty()){
                    System.out.println("phone empty");
                    phoneNumberErrorLabel.setText("Yêu cầu nhập thông tin");
                    phoneNumberErrorLabel.setTextFill(Color.RED);
                } else {
                    phoneNumberErrorLabel.setText("");

                }
            }
        }
        List<MenuButton> menuButtons = new ArrayList<>(List.of(genderMenuButton));
        for (MenuButton menuButton : menuButtons){
            if (menuButton == genderMenuButton){
                if (Objects.equals(menuButton.getText(), "Gender")){
                    genderErrorLabel.setTextFill(Color.RED);
                    genderErrorLabel.setText("Giới tính không hợp lệ");
                }
                else {
                    genderErrorLabel.setText("");

                }
            }
        }
    }
    public void submitButtonOnAction() {
        ApartmentState apartmentStateToSet;
        if (residentList.isEmpty()) {
            apartmentStateToSet = ApartmentState.AVAILABLE;
        } else {
            apartmentStateToSet = ApartmentState.OCCUPIED;
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
            if (apartmentStateToSet.equals(ApartmentState.OCCUPIED)){
                List<Collection> collections = collectionService.findAll();
                for (Collection collection : collections) {
                    if (collection.getType() == CollectionType.SERVICE_FEE || collection.getType() == CollectionType.MANAGEMENT_FEE) {
                        ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
                        Date date = Date.valueOf(this.residentList.getFirst().get("datePicker"));
                        if (date.getYear() + 1900 < LocalDate.now().getYear()){
                            LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, 30);
                            while(localDate.isBefore(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().plus(1), LocalDate.now().getDayOfMonth()))){
                                if ((30 - date.getDate()) > 5){
                                    apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, Date.valueOf(localDate)));
                                }
                                localDate=localDate.plusMonths(1);
                            }
                        }else {
                            for (int i = date.getMonth() ; i < LocalDate.now().getMonthValue() ; i++) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, LocalDate.now().getYear());
                                calendar.set(Calendar.DAY_OF_MONTH, 30);
                                if (date.getMonth() == date.getMonth()){
                                    calendar.set(Calendar.MONTH, i);
                                    java.util.Date date1 = calendar.getTime();
                                    if (30 - date.getDate() > 5){
                                        apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date1));
                                    }
                                }else {
                                    calendar.set(Calendar.MONTH, i);
                                    java.util.Date date1 = calendar.getTime();
                                    apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date1));
                                }
                            }
                        }
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
//                        calendar.set(Calendar.DAY_OF_MONTH, 30);
//                        for (int i = Date.valueOf(this.residentList.getFirst().get("datePicker")).getMonth() ; i < 12 ; i++){
//                            calendar.set(Calendar.MONTH, i);
//                            java.util.Date date =  calendar.getTime();
//                            apartmentCollectionService.merge(new ApartmentCollection(apartment, collection, date));
//                        }

                    }
                }
            }
            for (ObservableMap<String, String> observableMap : this.residentList) {
                Resident resident = new Resident(
                        observableMap.get("ID"),
                        Date.valueOf(observableMap.get("dob")),
                        observableMap.get("gender"),
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
        clearTextField(new ArrayList<>(List.of(IDTextField, lastNameTextField, firstNameTextField, phoneNumberTextField, nationalIDTextField, emailTextField, phoneNumberTextField)));
        apartmentTextField.setText(idTextField.getText());
        apartmentTextField.setDisable(true);
    }

    public void updateApartmentTableView() {
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
                .getResultList());
        totalResidentsCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidents().size()).asString());
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
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
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()));
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(apartments);
        apartmentTableView.setItems(apartmentObservableList);
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
    public void handleSubmitAddRes() {

        checkInvalidDate();
        checkInvalidInput();
        List<Label> errorList = new ArrayList<>(List.of(idErrorLabel, firstNameErrorLabel, lastNameErrorLabel, apartmentErrorLabel, dobErrorLabel, moveInDateErrorLabel, phoneNumberErrorLabel, emailErrorLabel, genderErrorLabel));
        boolean invalidFlag = false;
        for (Label label : errorList){
            System.out.println(label.getText());
            if (Objects.equals(label.getText(), "")){
                invalidFlag = false;
            } else {
                invalidFlag = true;
                break;
            }
        }
        System.out.println(invalidFlag);
        if (!invalidFlag){
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
                    IDTextField.setText(selectedResident.get("ID"));
                    dobPicker.setValue(Date.valueOf(selectedResident.get("dob")).toLocalDate());
                    datePicker.setValue(LocalDate.parse(selectedResident.get("datePicker")));
                    firstNameTextField.setText(selectedResident.get("firstName"));
                    lastNameTextField.setText(selectedResident.get("lastName"));
                    nationalIDTextField.setText(selectedResident.get("nationalID"));
                    phoneNumberTextField.setText(selectedResident.get("phoneNumber"));
                    emailTextField.setText(selectedResident.get("email"));
                }
            });
            residentTableView.setItems(residentList);
            dialogContainer.setVisible(false);
            addNewResContainer.setVisible(false);
            clearTextField(new ArrayList<>(List.of(IDTextField, lastNameTextField, firstNameTextField, phoneNumberTextField, nationalIDTextField, emailTextField)));

        } else {

        }

    }

    public void handleCloseAddNewRes() {
        dialogContainer.setVisible(false);
        addNewResContainer.setVisible(false);
        clearTextField(new ArrayList<>(List.of(IDTextField, lastNameTextField, firstNameTextField, phoneNumberTextField, nationalIDTextField, emailTextField)));

    }

    public void clearTextField(List<TextField> listTextFields) {
        for (TextField textField : listTextFields) {
            textField.setText("");
        }
        dobPicker.setValue(null);
        datePicker.setValue(null);
    }
}

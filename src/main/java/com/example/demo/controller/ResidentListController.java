package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.HibernateUtility;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanPropertyUtils;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class ResidentListController {

    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    public Button newButton;
    public TableView<Resident> residentTableView;
    public TableColumn<Resident, String> firstNameTableColumn;
    public TableColumn<Resident, String> lastNameTableColumn;
    public TableColumn<Resident, String> apartmentTableColumn;
    public Button deleteButton;
    public Button detailsButton;
    public TableColumn<Resident, Integer> idTableColumn;
    public Button updateButton;
    public Boolean switchViewFlag = false;
    public Resident residentToShow;
    public MenuItem IDNumberItem;

    public MenuItem apartmentItem;
    public MenuItem moveInDateItem;
    public ObservableMap<String, String> selectedFloor = FXCollections.observableHashMap();

    public MenuItem firstNameItem;
    public MenuItem genderItem;
    public MenuItem lastNameItem;
    public MenuItem residentIDItem;
    public TextField searchTextField;
    public MenuButton residentMenuButton;
    public MenuButton genderMenuButton;
    public MenuItem maleItem;
    public MenuItem femaleItem;
    public DatePicker dobPicker;
    public HBox searchContainer;
    public ScrollPane residentContainer;
    public TextField apartmentIDTextField;
    public TextField dobTextField;

    public TextField emailTextField;
    public TextField firstNameTextField;

    public TextField genderTextField;
    public TextField idNumberTextField;
    public TextField lastNameTextField;
    public TextField moveinDateTextField;
    public TextField nationalIDTextField;
    public TextField phoneNumberTextField;
    public TextField resIDTextField;
    public VBox detailContainer;
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());

    @FXML
    public void initialize() {
        List<Resident> residents = new ArrayList<>(residentService.findAll());
        fillTableViewWithResidentData(residents);
        enableDoubleClickForResidentDetails();

        wrapMenuItem(List.of(IDNumberItem, apartmentItem, moveInDateItem, genderItem, firstNameItem, lastNameItem, residentIDItem), residentMenuButton);
        wrapMenuItem(List.of(maleItem, femaleItem), genderMenuButton);
        searchTextField.textProperty().addListener((observable, oldValue, newVale) -> {
            List<Resident> filterList = new ArrayList<>();
            handleFilter(filterList, newVale, residents);
            fillTableViewWithResidentData(filterList);
        });
        residentMenuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (residentMenuButton.getText().equals("Giới tính")) {
                genderMenuButton.setVisible(true);
                searchContainer.setVisible(false);
                dobPicker.setVisible(false);
                searchTextField.setText("");
                genderMenuButton.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    List<Resident> filterList = new ArrayList<>();
                    if (newValue1.equals("Nữ")) {
                        for (Resident resident : residents) {
                            if (resident.getGender()) {
                                filterList.add(resident);
                            }
                        }
                    } else {
                        for (Resident resident : residents) {
                            if (!resident.getGender()) {
                                filterList.add(resident);
                            }
                        }
                    }
                    fillTableViewWithResidentData(filterList);
                });
            } else if (residentMenuButton.getText().equals("Ngày chuyển vào")) {
                dobPicker.setVisible(true);
                searchContainer.setVisible(false);
                genderMenuButton.setVisible(false);
                searchTextField.setText("");

                dobPicker.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    List<Resident> filterList = new ArrayList<>();
                    for (Resident resident : residents) {
                        if (Objects.equals(resident.getMoveInDate(), Date.valueOf(newValue1))) {
                            filterList.add(resident);
                        }
                    }
                    fillTableViewWithResidentData(filterList);
                });
            } else {
                dobPicker.setVisible(false);
                searchContainer.setVisible(true);
                genderMenuButton.setVisible(false);
                if (searchTextField.getText().isEmpty()) {
                    fillTableViewWithResidentData(FXCollections.observableList(residents));
                } else {
                    List<Resident> filterList = new ArrayList<>();
                    handleFilter(filterList, searchTextField.getText(), residents);
                    fillTableViewWithResidentData(filterList);
                }
            }
        });
        setFormatter();
    }

    public void showResidentDetailFromAnotherView(Resident resident) {
        residentToShow = resident;
        handleShowResidentDetail(residentToShow);
        detailContainer.setVisible(true);
        updateButton.setVisible(true);
        switchViewFlag = true;
    }
    public void handleShowResidentDetail(Resident resident){
        resIDTextField.setText(String.valueOf(resident.getId()));
        idNumberTextField.setText(resident.getIDNumber());
        firstNameTextField.setText(resident.getFirstName());
        lastNameTextField.setText(resident.getLastName());
        apartmentIDTextField.setText(resident.getApartmentID());
        apartmentIDTextField.setDisable(true);
        dobTextField.setText(resident.getDateOfBirth().toString());
        emailTextField.setText(resident.getEmail());
        genderTextField.setText(resident.getGender() ? "Nữ" : "Nam");
        moveinDateTextField.setText(resident.getMoveInDate().toString());
        nationalIDTextField.setText(resident.getNationalID());
        phoneNumberTextField.setText(resident.getPhoneNumber());

    }
    public void wrapMenuItem(List<MenuItem> list, MenuButton menuButton) {
        for (MenuItem menuItem : list) {
            menuItem.setOnAction(e -> menuButton.setText(menuItem.getText()));
        }
    }

    private void fillTableViewWithResidentData(List<Resident> residentList) {
        residentTableView.setItems(FXCollections.observableList(residentList));
        idTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        firstNameTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFirstName()));
        lastNameTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLastName()));
        apartmentTableColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getApartmentID()));
    }

    private void enableDoubleClickForResidentDetails() {
        residentTableView.setRowFactory(tableView -> {
            TableRow<Resident> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    detailsButtonOnAction();
                }
            });
            return row;
        });
    }

    public void newButtonOnAction() {
        MenuViewManager.switchView(MenuView.RESIDENT_FORM);
    }

    public void deleteButtonOnAction() {
        Resident resident = residentTableView.getSelectionModel().getSelectedItem();

        if (resident == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Hãy chọn dân cư để xem thông tin chi tiết");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa ?");
            alert.setContentText("Nhấn OK để xóa hoặc Cancel để hủy");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                        residentTableView.getItems().remove(resident);
                        residentService.remove(resident);
                }
            });
        }

    }

    public void detailsButtonOnAction() {
        Resident resident = residentTableView.getSelectionModel().getSelectedItem();
        if (resident == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Hãy chọn dân cư để xem thông tin chi tiết");
            alert.showAndWait();
        }else {
            detailContainer.setVisible(true);
            updateButton.setVisible(true);
            handleShowResidentDetail(resident);
        }

    }

    public void handleFilter(List<Resident> filterList, String newValue, List<Resident> residents) {
        switch (residentMenuButton.getText()) {
            case "ID dân cư":
                for (Resident resident : residents) {
                    if (String.valueOf(resident.getId()).contains(newValue)) {
                        filterList.add(resident);
                    }
                }
                break;
            case "Họ":
                for (Resident resident : residents) {
                    if (resident.getFirstName().contains(newValue)) {
                        filterList.add(resident);
                    }
                }
                break;
            case "Tên":
                for (Resident resident : residents) {
                    if (resident.getLastName().contains(newValue)) {
                        filterList.add(resident);
                    }
                }
                break;
            case "Căn hộ":
                for (Resident resident : residents) {
                    if (resident.getApartmentID().contains(newValue)) {
                        filterList.add(resident);
                    }
                }
                break;
            case "Số CCCD":
                for (Resident resident : residents) {
                    if (resident.getIDNumber().contains(newValue)) {
                        filterList.add(resident);
                    }
                }
                break;
            default:
                filterList.addAll(residents);
                break;
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
        idNumberTextField.setTextFormatter(IDNumberFormatter);
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
    public void handleUpdateResident(Resident selectedResident){
        boolean id = true;
        boolean phone = true;
        boolean dob = true;
        boolean date = true;
        if(idNumberTextField.getText().length() != 12){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("CCCD phải có 12 số");
            alert.setContentText("Nhấn OK để tắt thông báo");
            id = false;
            alert.showAndWait();
        } else {
            selectedResident.setIDNumber(idNumberTextField.getText());
        }
        selectedResident.setFirstName(firstNameTextField.getText());
        selectedResident.setLastName(lastNameTextField.getText());
        selectedResident.setEmail(emailTextField.getText());
        selectedResident.setGender(genderTextField.equals("Nữ"));
        try{
            selectedResident.setDateOfBirth(Date.valueOf(dobTextField.getText()));
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ngày sinh không hợp lệ");
            alert.setContentText("Nhấn OK để tắt thông báo");
            dob = false;

            alert.showAndWait();
        }
        try{
            selectedResident.setMoveInDate(Date.valueOf(moveinDateTextField.getText()));

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ngày sinh không hợp lệ");
            alert.setContentText("Nhấn OK để tắt thông báo");
            date = false;
            alert.showAndWait();
        }
        if (phoneNumberTextField.getText().length() != 10){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Số điện thoại phải có 10 số");
            alert.setContentText("Nhấn OK để tắt thông báo");
            phone = false;
            alert.showAndWait();
        } else {
            selectedResident.setPhoneNumber(phoneNumberTextField.getText());
        }
        selectedResident.setNationalID(nationalIDTextField.getText());
        if (!id || !phone || !date || !dob){

        } else {
            residentService.merge(selectedResident);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Cập nhật thông tin thành công");
            alert.setContentText("Nhấn OK để tắt thông báo");
            alert.showAndWait();
        }
        residentTableView.refresh();
    }
    public void updateButtonOnAction() {
        detailContainer.setVisible(true);
        updateButton.setVisible(true);
        Resident resident = residentTableView.getSelectionModel().getSelectedItem();
        if (resident != null) {
            handleUpdateResident(resident);
        } else if (switchViewFlag) {
            handleUpdateResident(residentToShow);
            MenuViewManager.switchViewFromResidentListToShowApartmentDetail(MenuView.APARTMENT_LIST, residentToShow, selectedFloor);
        }
    }

}

package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Collection;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanPropertyUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResidentListController {

    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    public Button newButton;
    public TableView<Resident> residentTableView;
    public TableColumn<Resident, String> firstNameTableColumn;
    public TableColumn<Resident, String> lastNameTableColumn;
    public TableColumn<Resident, String> apartmentTableColumn;
    public Button deleteButton;
    public PropertySheet residentPropertySheet;
    public Button detailsButton;
    public TableColumn<Resident, Integer> idTableColumn;
    public Button updateButton;
    public Boolean switchViewFlag = false;
    public Resident residentToShow;
    public MenuItem IDNumberItem;

    public MenuItem apartmentItem;
    public MenuItem moveInDateItem;

    public MenuItem firstNameItem;
    public MenuItem genderItem;
    public MenuItem lastNameItem;
    public MenuItem residentIDitem;
    public TextField searchTextField;
    public MenuButton residentMenuButton;
    public MenuButton genderMenuButton;
    public MenuItem maleItem;
    public MenuItem femaleItem;
    public DatePicker dobPicker;
    public HBox searchContainer;

    @FXML
    public void initialize() {
        List <Resident> residents = new ArrayList<>(residentService.findAll());
        fillTableViewWithResidentData(residents);
        enableDoubleClickForResidentDetails();
        wrapMenuItem(List.of(IDNumberItem, apartmentItem, moveInDateItem, genderItem, firstNameItem, lastNameItem, residentIDitem), residentMenuButton);
        wrapMenuItem(List.of(maleItem, femaleItem), genderMenuButton);
        searchTextField.textProperty().addListener((observable, oldValue, newVale) -> {
            List<Resident> filterList = new ArrayList<>();
            handleFilter(filterList, newVale, residents);
            fillTableViewWithResidentData(filterList);
        });
        residentMenuButton.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(residentMenuButton.getText().equals("Gender")){
                genderMenuButton.setVisible(true);
                searchContainer.setVisible(false);
                dobPicker.setVisible(false);
                searchTextField.setText("");
                genderMenuButton.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    List<Resident> filterList = new ArrayList<>();
                    if(newValue1.equals("Female")){
                        for (Resident resident : residents){
                            if (resident.getGender()){
                                filterList.add(resident);
                            }
                        }
                    }else {
                        for (Resident resident : residents){
                            if (!resident.getGender()){
                                filterList.add(resident);
                            }
                        }
                    }
                    fillTableViewWithResidentData(filterList);
                });
            } else if(residentMenuButton.getText().equals("Move-in Date")){
                dobPicker.setVisible(true);
                searchContainer.setVisible(false);
                genderMenuButton.setVisible(false);
                searchTextField.setText("");

                dobPicker.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
                    List<Resident> filterList = new ArrayList<>();
                    for (Resident resident : residents){
                        if(Objects.equals(resident.getMoveInDate(), Date.valueOf(newValue1))){
                            filterList.add(resident);
                        }
                    }
                    fillTableViewWithResidentData(filterList);
                });
            }else {
                dobPicker.setVisible(false);
                searchContainer.setVisible(true);
                genderMenuButton.setVisible(false);
                if(searchTextField.getText().isEmpty()){
                    fillTableViewWithResidentData(FXCollections.observableList(residents));
                } else {
                    List<Resident> filterList = new ArrayList<>();
                    handleFilter(filterList, searchTextField.getText(), residents);
                    fillTableViewWithResidentData(filterList);
                }
            }
        });
    }
    public void showResidentDetailFromAnotherView(Resident resident){
        residentToShow = resident;
        residentPropertySheet.getItems().clear();
        residentPropertySheet.getItems().addAll(BeanPropertyUtils.getProperties(residentToShow));
        switchViewFlag = true;
    }
    public void wrapMenuItem(List<MenuItem> list, MenuButton menuButton){
        for(MenuItem menuItem : list){
            menuItem.setOnAction(e -> {
                menuButton.setText(menuItem.getText());
            });
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
        if (resident != null) {
            residentTableView.getItems().remove(resident);
            residentService.remove(resident);
        }
    }

    public void detailsButtonOnAction() {
        residentPropertySheet.getItems().clear();
        Resident resident = residentTableView.getSelectionModel().getSelectedItem();
        residentPropertySheet.getItems().addAll(BeanPropertyUtils.getProperties(resident));
    }
    public void handleFilter(List<Resident> filterList, String newValue, List<Resident> residents){
        switch (residentMenuButton.getText()){
            case "Resident ID":
                for(Resident resident : residents){
                    if(String.valueOf(resident.getId()).contains(newValue)){
                        filterList.add(resident);
                    }
                }
                break;
            case "First Name":
                for(Resident resident : residents){
                    if(resident.getFirstName().contains(newValue)){
                        filterList.add(resident);
                    }
                }
                break;
            case "Last Name":
                for(Resident resident : residents){
                    if(resident.getLastName().contains(newValue)){
                        filterList.add(resident);
                    }
                }
                break;
            case "Apartment":
                for(Resident resident : residents){
                    if(resident.getApartmentID().contains(newValue)){
                        filterList.add(resident);
                    }
                }
                break;
            case "ID Number":
                for(Resident resident : residents){
                    if(resident.getIDNumber().contains(newValue)){
                        filterList.add(resident);
                    }
                }
                break;
            default:
                filterList.addAll(residents);
                break;
        }
    }
    public void updateButtonOnAction() {
        Resident resident = residentTableView.getSelectionModel().getSelectedItem();
        if (resident != null) {
            updateButton.setDisable(true);
            residentService.merge(resident);
            residentTableView.refresh();
        }
        else{
            if(switchViewFlag){
                residentService.merge(residentToShow);
                residentTableView.refresh();
                MenuViewManager.switchViewFromResidentListToShowApartmentDetail(MenuView.APARTMENT_LIST, residentToShow, null);
            }
        }
    }

    public void enableUpdateButton() {
        updateButton.setDisable(false);
    }
}

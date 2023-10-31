package com.example.demo.controller;

import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResidentListController {

    private final ResidentService residentService = new ResidentService(new ResidentRepository());
    public Button newButton;
    public TableView<Resident> residentTableView;
    public TableColumn<Resident, String> firstNameTableColumn;
    public TableColumn<Resident, String> lastNameTableColumn;
    public TableColumn<Resident, String> apartmentTableColumn;
    public TableColumn<Resident, String> phoneNumberTableColumn;
    public TableColumn<Resident, String> emailTableColumn;
    public TableColumn<Resident, String> nationalIDTableColumn;
    public Button deleteButton;

    @FXML
    public void initialize() {
        residentTableView.setItems(FXCollections.observableList(residentService.findAll()));
        firstNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirstName()));
        lastNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLastName()));
        apartmentTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getApartment().getId()));
        phoneNumberTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPhoneNumber()));
        emailTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getEmail()));
        nationalIDTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNationalID()));
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
}

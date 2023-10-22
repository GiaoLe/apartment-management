package com.example.demo.controller;

import com.example.demo.dao.Resident;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResidentListController {

    public Button newResidentButton;
    public TableView<Resident> residentTableView;
    public TableColumn<Resident, String> firstNameTableColumn;
    public TableColumn<Resident, String> lastNameTableColumn;
    public TableColumn<Resident, String> apartmentTableColumn;
    public TableColumn<Resident, String> phoneNumberTableColumn;
    public TableColumn<Resident, String> emailTableColumn;
    public TableColumn<Resident, String> nationalIDTableColumn;

    @FXML
    public void initialize() {
        ResidentService residentService = new ResidentService(new ResidentRepository());
        residentTableView.setItems(FXCollections.observableList(residentService.findAll()));
        firstNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getFirstName()));
        lastNameTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLastName()));
        apartmentTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getApartment().getNumber()));
        phoneNumberTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPhoneNumber()));
        emailTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getEmail()));
        nationalIDTableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNationalID()));
    }

    public void newResidentButtonOnAction() {
        SceneManager.switchScene(Scene.RESIDENT_FORM.getFileName());
    }
}

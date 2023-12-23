package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.service.ResidentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanPropertyUtils;

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
    private Boolean switchViewFlag = false;
    private Resident residentToShow;
    @FXML
    public void initialize() {
        fillTableViewWithResidentData();
        enableDoubleClickForResidentDetails();
    }
    public void showResidentDetailFromAnotherView(Resident resident){
        residentToShow = resident;
        residentPropertySheet.getItems().clear();
        residentPropertySheet.getItems().addAll(BeanPropertyUtils.getProperties(residentToShow));
        switchViewFlag = true;
    }
    private void fillTableViewWithResidentData() {

        residentTableView.setItems(FXCollections.observableList(residentService.findAll()));
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
                MenuViewManager.switchViewFromResidentListToShowApartmentDetail(MenuView.APARTMENT_LIST, residentToShow);
            }
        }
    }

    public void enableUpdateButton() {
        updateButton.setDisable(false);
    }
}

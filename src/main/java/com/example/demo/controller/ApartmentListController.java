package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.ApartmentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApartmentListController {


    public TableColumn<?, ?> actionsColumn;


    public TableView<String> apartmentTableView;


    public TableColumn<String, String> availableColumn;


    public Button deleteButton;


    public TableColumn<String, String> floorColumn;


    public TableColumn<String, String> nAvailableColumn;


    public Button newButton;


    public TableColumn<String, String> occupiedColumn;

    public TableColumn<String, String> residentsColumn;


    public TableColumn<String, String> totalColumn;
    private final ApartmentService apartmentService = new ApartmentService(new ApartmentRepository());
    private final Map<String, String> floorDetails = new HashMap<>();
    public void updateFloorDetails() {
        floorDetails.put("totalApartments", HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("select count(*) from Apartment ", Long.class)
                .uniqueResult()).toString());
        floorDetails.put("totalResidents", HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("select sum(number) from Apartment ", Long.class)
                .uniqueResult()).toString());
      //continue
    }
    public void showFloorList() {


    }
    public void initialize() {
//        apartmentTableView.setItems(FXCollections.observableList(apartmentService.findAll()));
//        numberTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumber()));
//        areaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
//        totalRoomsTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRoomCount()).asString());
//        floorTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFloor()));
//        totalResidentsTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalResidents()));
        showFloorList();
    }

    public void newButtonOnAction() {
        MenuViewManager.switchView(MenuView.APARTMENT_FORM);
    }

    public void deleteButtonOnAction(ActionEvent actionEvent) {
    }
}

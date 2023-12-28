package com.example.demo.gui;

import com.example.demo.Main;
import com.example.demo.controller.*;
import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentCollection;
import com.example.demo.dao.Resident;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class MenuViewManager {

    @Setter
    private static BorderPane borderPane;

    private MenuViewManager() {}

    public static Object switchView(MenuView menuView) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            borderPane.setCenter(loader.load());
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void switchViewToShowResidentDetails(MenuView menuView, Resident resident){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            Parent root = loader.load();
            borderPane.setCenter(root);
            ResidentListController residentListController = loader.getController();
            residentListController.showResidentDetailFromAnotherView(resident);
            residentListController.searchTextField.setText(resident.getId().toString());
            residentListController.residentMenuButton.setText("Resident ID");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void switchViewFromResidentListToShowApartmentDetail(MenuView menuView, Resident resident, ObservableMap<String, String> selectedFloor){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            Parent root = loader.load();
            borderPane.setCenter(root);
            ApartmentListController apartmentListController = loader.getController();
            String floor = resident.getApartmentID().substring(0, 1);
            apartmentListController.showFloorDetail(Integer.parseInt(floor));
            apartmentListController.selectedFloor = selectedFloor;
            apartmentListController.apartmentTableView.setVisible(true);
            apartmentListController.floorTableView.setVisible(false);
            apartmentListController.apartmentTableView.setVisible(true);
            apartmentListController.backButton.setVisible(true);
            apartmentListController.backButton.setOnMouseClicked(event -> {
                apartmentListController.floorTableView.setVisible(true);
                apartmentListController.apartmentTableView.setVisible(false);
                apartmentListController.backButton.setVisible(false);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void switchViewToShowCollectionDetail(MenuView menuView, ApartmentCollection apartmentCollection, Apartment selectedApartment){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            Parent root = loader.load();
            CollectionReportController collectionFormController = loader.getController();
            collectionFormController.initializeData(apartmentCollection.getCollection());
            collectionFormController.residentMenuButton.setText("ApartmentID");
            collectionFormController.searchTextField.setText(selectedApartment.getId());
            collectionFormController.switchViewFlag = true;
            collectionFormController.apartmentCollection = apartmentCollection;
            borderPane.setCenter(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void switchViewFromCollectionReportToApartmentDetail(MenuView menuView, ApartmentCollection apartmentCollection){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            Parent root = loader.load();
            ApartmentListController apartmentListController = loader.getController();
            apartmentListController.showFloorDetail(Integer.parseInt(apartmentCollection.getApartment().getId().substring(0, 1)));
            apartmentListController.selectedFloor.put("floor", apartmentCollection.getApartment().getId().substring(0, 1));
            apartmentListController.toggleFilter();
            apartmentListController.backButton.setOnMouseClicked(e -> {
                apartmentListController.floorTableView.setVisible(true);
                apartmentListController.apartmentTableView.setVisible(false);
                apartmentListController.backButton.setVisible(false);
            });
            apartmentListController.apartmentTableView.setVisible(true);
            apartmentListController.floorTableView.setVisible(false);
            apartmentListController.apartmentTableView.setVisible(true);
            apartmentListController.backButton.setVisible(true);
            borderPane.setCenter(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void switchViewToAddNewRes(MenuView menuView, Apartment apartment,  ObservableMap<String, String> selectedFloor){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            Parent root = loader.load();
            ResidentFormController residentFormController = loader.getController();
            residentFormController.apartmentTextField.setText(apartment.getId());
            residentFormController.switchViewFlag = true;
            residentFormController.selectedFloor = selectedFloor;
            borderPane.setCenter(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

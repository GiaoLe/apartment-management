package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MenuController {

    public Button dashboardButton;
    public TableColumn<Apartment, Integer> availableRooms;
    public TableColumn<?, ?> description;
    public TableColumn<?, ?> flatNumber;
    public TableColumn<?, ?> notAvailableRooms;
    public TableColumn<?, ?> occupiedRooms;
    public TableColumn<?, ?> totalResidents;
    public TableColumn<?, ?> totalRooms;
    public Button residentsButton;
    public BorderPane borderPane;
    public Button apartmentsButton;
    public Button collectionsButton;
    public Label dateLabel;
    public void date() {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        String monthName = localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        int day = localDate.getDayOfMonth();
        dateLabel.setText(monthName + " " + day + ", " + year);
    }
    public void handleClickSideBar(ActionEvent actionEvent) {
        String handleClick = ((Button) actionEvent.getSource()).getId();
        switch (handleClick){
            case "residentsButton":
                residentsButton.getStyleClass().add("selected-item");
                MenuViewManager.switchView(MenuView.RESIDENT_LIST);
                residentsButton.getStyleClass().add("button-clicked");
                apartmentsButton.getStyleClass().remove("selected-item");
                collectionsButton.getStyleClass().remove("selected-item");
                dashboardButton.getStyleClass().remove("selected-item");
                break;
            case "apartmentsButton":
                residentsButton.getStyleClass().remove("selected-item");
                apartmentsButton.getStyleClass().add("selected-item");
                MenuViewManager.switchView(MenuView.APARTMENT_LIST);
                collectionsButton.getStyleClass().remove("selected-item");
                dashboardButton.getStyleClass().remove("selected-item");
                break;
            case "collectionsButton":
                residentsButton.getStyleClass().remove("selected-item");
                apartmentsButton.getStyleClass().remove("selected-item");
                collectionsButton.getStyleClass().add("selected-item");
                MenuViewManager.switchView(MenuView.COLLECTION_LIST);
                dashboardButton.getStyleClass().remove("selected-item");
                break;
            case "dashboardButton":
                residentsButton.getStyleClass().remove("selected-item");
                apartmentsButton.getStyleClass().remove("selected-item");
                collectionsButton.getStyleClass().remove("selected-item");
                dashboardButton.getStyleClass().add("selected-item");
                MenuViewManager.switchView(MenuView.DASHBOARD);

        }

    }
    @FXML
    public void initialize() {
        MenuViewManager.setBorderPane(borderPane);
        date();
    }
}

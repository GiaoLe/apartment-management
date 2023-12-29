package com.example.demo.controllers;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MenuController {
    public Button dashboardButton;
    public Button residentsButton;
    public BorderPane borderPane;
    public Button apartmentsButton;
    public Button collectionsButton;
    public Label dateTimeLabel;
    public VBox sideBar;
    public Label userIDLabel;
    private List<Button> buttonList;

    @FXML
    public void initialize() {
        MenuViewManager.setBorderPane(borderPane);
        displayLiveTime();
        initializeUIDDisplay();
        initializeListOfMenuButtons();
        borderPane.centerProperty().addListener(e -> {
            switch (borderPane.centerProperty().get().getId()){
                case "apartmentContainer":
                    clearAllButtonStyles();
                    apartmentsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "residentContainer":
                    clearAllButtonStyles();

                    residentsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "collectionContainer", "collectionReportContainer":
                    clearAllButtonStyles();
                    collectionsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "dashboardContainer":
                    clearAllButtonStyles();
                    dashboardButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
            }
        });
    }

    private void initializeListOfMenuButtons() {
        buttonList = new ArrayList<>(List.of(dashboardButton, apartmentsButton, collectionsButton, residentsButton));
    }

    private void initializeUIDDisplay() {
        userIDLabel.setText("UID: " + LoginController.getCurrentUserID());
    }

    private void displayLiveTime() {
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> dateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void setButtonStyleAndSwitchView(Button button, MenuView menuView) {
        clearAllButtonStyles();
        button.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
        MenuViewManager.switchView(menuView);
    }

    public void clearAllButtonStyles() {
        for (Button button : buttonList) {
            button.setStyle("");
        }
    }

    public void apartmentsButtonOnAction() {
        setButtonStyleAndSwitchView(apartmentsButton, MenuView.APARTMENT_LIST);
    }

    public void dashboardButtonOnAction() {
        setButtonStyleAndSwitchView(dashboardButton, MenuView.DASHBOARD);
    }

    public void residentsButtonOnAction() {
        setButtonStyleAndSwitchView(residentsButton, MenuView.RESIDENT_LIST);
    }

    public void collectionsButtonOnAction() {
        setButtonStyleAndSwitchView(collectionsButton, MenuView.COLLECTION_LIST);
    }
}

package com.example.demo.controller;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
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

    @FXML
    public void initialize() {
        MenuViewManager.setBorderPane(borderPane);
        displayLiveTime();
        userIDLabel.setText("UID: " + LoginController.getCurrentUserID());
        setSelectedBtn(dashboardButton, MenuView.DASHBOARD);
        setSelectedBtn(apartmentsButton, MenuView.APARTMENT_LIST);
        setSelectedBtn(collectionsButton, MenuView.COLLECTION_LIST);
        setSelectedBtn(residentsButton, MenuView.RESIDENT_LIST);
        borderPane.centerProperty().addListener(e -> {
            switch (borderPane.centerProperty().get().getId()){
                case "apartmentContainer":
                    clearUnderline();
                    apartmentsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "residentContainer":
                    clearUnderline();

                    residentsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "collectionContainer", "collectionReportContainer":
                    clearUnderline();
                    collectionsButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
                case "dashboardContainer":
                    clearUnderline();
                    dashboardButton.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
                    break;
            }
        });
    }

    private void displayLiveTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> dateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void setSelectedBtn (Button button, MenuView menuView){
        button.setOnAction(e -> {
            clearUnderline();
            button.setStyle("-fx-text-fill: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #f20000 0.0%, #f20000 20.6376%, #0ab6e1 100.0%);");
            MenuViewManager.switchView(menuView);
        });
    }
    public void clearUnderline(){
        List<Button> buttonList = new ArrayList<>(List.of(dashboardButton, apartmentsButton, collectionsButton, residentsButton));
        for (Button button : buttonList){
            button.setStyle("");
        }
    }
}

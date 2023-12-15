package com.example.demo.controller;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuController {
    public ImageView toggleIcon;
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
    }

    private void displayLiveTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> dateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    public void toggleSidebar() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), sideBar);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(toggleIcon.rotateProperty(), 0)),
                new KeyFrame(Duration.seconds(0.25), new KeyValue(toggleIcon.rotateProperty(), 180))
        );
        timeline.play();
        if (sideBar.getTranslateX() == 0) {
            translateTransition.setToX(-sideBar.getPrefWidth() + sideBar.getPrefWidth()/3);

        } else {
            translateTransition.setToX(0);

        }

        translateTransition.play();
    }

    public void dashboardButtonOnAction() {
        MenuViewManager.switchView(MenuView.DASHBOARD);
    }

    public void residentsButtonOnAction() {
        MenuViewManager.switchView(MenuView.RESIDENT_LIST);
    }

    public void collectionsButtonOnAction() {
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }

    public void apartmentsButtonOnAction() {
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }
}

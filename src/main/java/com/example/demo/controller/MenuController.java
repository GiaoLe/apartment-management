package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MenuController {
    public ImageView toggleIcon;
    public Button dashboardButton;
    public Button slideBtn;
    public Button residentsButton;
    public BorderPane borderPane;
    public Button apartmentsButton;
    public Button collectionsButton;
    public Label dateLabel;
    public VBox sideBar;
    public void date() {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        String monthName = localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        int day = localDate.getDayOfMonth();
        dateLabel.setText(monthName + " " + day + ", " + year);
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
@FXML
    public void initialize() {
        MenuViewManager.setBorderPane(borderPane);
        date();
    }

    public void dashboardButtonOnAction(ActionEvent actionEvent) {
        MenuViewManager.switchView(MenuView.DASHBOARD);
    }

    public void residentsButtonOnAction(ActionEvent actionEvent) {
        MenuViewManager.switchView(MenuView.RESIDENT_LIST);
    }

    public void collectionsButtonOnAction(ActionEvent actionEvent) {
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }

    public void apartmentsButtonOnAction(ActionEvent actionEvent) {
        MenuViewManager.switchView(MenuView.APARTMENT_LIST);
    }
}

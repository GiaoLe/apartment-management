package com.example.demo.controller;

import com.example.demo.Scene;
import com.example.demo.SceneManager;
import com.example.demo.dao.Apartment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button btnApartment;

    @FXML
    private Button btnDashboard;
    @FXML
    private TableColumn<Apartment, Integer> availableRooms;
    @FXML
    private TableColumn<?, ?> description;

    @FXML
    private TableView<?> flatList;

    @FXML
    private TableColumn<?, ?> flatNumber;

    @FXML
    private TableColumn<?, ?> notAvailableRooms;

    @FXML
    private TableColumn<?, ?> occupiedRooms;

    @FXML
    private TableColumn<?, ?> totalResidents;

    @FXML
    private TableColumn<?, ?> totalRooms;
    public void clickNavigation(ActionEvent actionEvent) {
        String btnID = ((Button) actionEvent.getSource()).getId();
        if(btnID.contains("btnDashboard")){
            btnDashboard.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            btnApartment.setStyle("-fx-text-fill: #979191;" + "-fx-underline: false");
        }else if(btnID.contains("btnApartment")){
            btnApartment.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            btnDashboard.setStyle("-fx-text-fill: #979191;" + "-fx-underline: false");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.Scene;
import com.example.demo.SceneManager;
import com.example.demo.dao.Apartment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button btnApartment;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnSearch;
    @FXML
    private TableColumn<Apartment, String> description;

    @FXML
    private TableView<?> flatList;

    @FXML
    private TableColumn<Apartment, Integer> floorNumber;

    @FXML
    private TableColumn<Apartment, Integer> notAvailableApartments;

    @FXML
    private TableColumn<Apartment, Integer> occupiedApartments;
    @FXML
    private TableColumn<Apartment, Integer> availableApartments;
    @FXML
    private TableColumn<Apartment, Integer> totalApartments;

    @FXML
    private TableColumn<Apartment, Integer> totalResidents;
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
    public void showFlatList() {
        List<Integer> floors = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("select distinct  floorNumber from Apartment ", Integer.class)
                .getResultList());
        for(Integer index : floors){
            floorNumber.setCellValueFactory(new PropertyValueFactory<>("index"));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showFlatList();
    }
}

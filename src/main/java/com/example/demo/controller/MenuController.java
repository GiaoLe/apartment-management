package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.dao.Apartment;
import com.example.demo.repository.ApartmentRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class MenuController {

    public Button apartmentButton;

    public Button dashboardButton;
    public TableColumn<Apartment, Integer> availableRooms;
    public TableColumn<Apartment, String> description;
    public TableView<Apartment> flatList;
    public TableColumn<Apartment, Integer> flatNumber;
    public TableColumn<Apartment, Integer> notAvailableRooms;
    public TableColumn<Apartment, Integer> occupiedRooms;
    public TableColumn<Apartment, Integer> totalResidents;
    public TableColumn<Apartment, Integer> totalRooms;
    public void clickNavigation(ActionEvent actionEvent){
        String btnID = ((Button) actionEvent.getSource()).getId();
        if(btnID.contains("dashboardButton")){
            dashboardButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            apartmentButton.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: #979191;");
        }else if(btnID.contains("apartmentButton")){
            apartmentButton.setStyle("-fx-text-fill: #ffa600;" + "-fx-underline: true");
            dashboardButton.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: #979191;");
        }
    }
    public List<Apartment> dataList(){
        List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment ", Apartment.class)
                .getResultList());
        return apartments;
    }
    @FXML
    public void initialize() {
        for(Apartment apartment : dataList()){
            System.out.println(apartment);
        }
    }
}

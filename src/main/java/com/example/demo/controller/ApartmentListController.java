package com.example.demo.controller;
import com.example.demo.dao.Resident;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import com.example.demo.dao.ApartmentState;
import com.example.demo.repository.HibernateUtility;
import com.example.demo.dao.Apartment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;

public class ApartmentListController {
    public Label floorText;


    public Label tableHeader;
    public Button backBtn;
    public TableView<ObservableMap<String, String>> floorTableView;
    public TableColumn<ObservableMap<String, String>, String> occupiedColumn;
    public TableColumn<ObservableMap<String, String>, String> residentsColumn;
    public TableColumn<ObservableMap<String, String>, String> totalColumn;
    public TableColumn<ObservableMap<String, String>, String> floorColumn;
    public TableColumn<ObservableMap<String, String>, String> nAvailableColumn;
    public TableView<Apartment> apartmentTableView;
    public TableColumn<ObservableMap<String, String>, String> availableColumn;
    private ObservableList<ObservableMap<String, String>> floorList;
    private final ObservableMap<String, String> firstFloor = FXCollections.observableHashMap();

    public AnchorPane dialogContainer;
    public AnchorPane dialogBox;
    private final ObservableMap<String, String> secondFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> thirdFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fourthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fifthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> sixthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> seventhFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> eighthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> ninthFloor = FXCollections.observableHashMap();
    public MenuItem availableItem;
    public MenuItem duplexItem;
    public MenuItem maintainingItem;
    public MenuItem occupiedItem;
    public MenuItem penthouseItem;
    public MenuItem studioItem;
    public MenuItem triplexItem;
    public MenuItem reservedItem;
    public Button filterBtn;
    public TextField apartmentIDFilter;
    public TextField hostNameFilter;
    public MenuButton stateMenu;
    public MenuButton typeMenu;

    private final ObservableMap<String, String> tenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> eleventhFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> twelfthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> thirteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fourteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> fifteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> sixteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> seventeenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> eighteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> nineteenthFloor = FXCollections.observableHashMap();
    private final ObservableMap<String, String> twentiethFloor = FXCollections.observableHashMap();
    public Button closeDialogBtn;
    public TableColumn<Apartment, String> actionsCol;
    public TableColumn<Apartment, String> apartmentIdCol;
    public TableColumn<Apartment, Double> areaCol;
    public TableColumn<Apartment, String> hostNameCol;
    public TableColumn<Apartment, String> stateCol;
    public TableColumn<Apartment, String> totalResidentsCol;
    public TableColumn<Apartment, String> typeCol;
    List<Apartment> apartments = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Apartment order by id asc ", Apartment.class)
            .getResultList());
    ObservableMap<String, String> selectedFloor;
    public Button submitFilter;
    public Button apartmentInfoClosebtn;
    public AnchorPane apartmentInfoDialog;
    public Label apartmentInfoID;
    public Label hostNameInfo;
    public Label stateInfo;
    public Label typeInfo;
    public TableColumn<Resident, String> email;
    public TableColumn<Resident, String> lastName;
    public TableColumn<Resident, String> nationalID;
    public TableColumn<Resident, String> phoneNumber;
    public TableColumn<Resident, String> residentID;
    public TableView<Resident> residentTableView;
    public Button addResBtn;
    public Button delResBtn;
    public Button editResBtn;
    public void updateFloorDetails() {
         floorList = FXCollections.observableArrayList();
        for(Apartment apartment : apartments){
            int numberOfApartment = 0;
            if(apartment.getFloor() == 1) {
                if (firstFloor.get("floor") == null) {
                    firstFloor.put("floor", "1");
                    firstFloor.put("totalApartments", "1");
                    updateAppsState(apartment, firstFloor);
                } else {
                    numberOfApartment = Integer.parseInt(firstFloor.get("totalApartments")) + 1;
                    firstFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        firstFloor.put("availableApartments", String.valueOf(Integer.parseInt(firstFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        firstFloor.put("oApartments", String.valueOf(Integer.parseInt(firstFloor.get("oApartments")) + 1));
                    } else {
                        firstFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(firstFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
                else if(apartment.getFloor() == 2){
                if(secondFloor.get("floor") == null){
                    secondFloor.put("floor", "2");
                    secondFloor.put("totalApartments", "1");
                    updateAppsState(apartment, secondFloor);
                }else{
                    numberOfApartment = Integer.parseInt(secondFloor.get("totalApartments")) + 1;
                    secondFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        secondFloor.put("availableApartments", String.valueOf(Integer.parseInt(secondFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        secondFloor.put("oApartments", String.valueOf(Integer.parseInt(secondFloor.get("oApartments")) + 1));
                    } else {
                        secondFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(secondFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 3){
                if(thirdFloor.get("floor") == null){
                    thirdFloor.put("floor", "3");
                    thirdFloor.put("totalApartments", "1");
                    updateAppsState(apartment, thirdFloor);
                }else{
                    numberOfApartment = Integer.parseInt(thirdFloor.get("totalApartments")) + 1;
                    thirdFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        thirdFloor.put("availableApartments", String.valueOf(Integer.parseInt(thirdFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        thirdFloor.put("oApartments", String.valueOf(Integer.parseInt(thirdFloor.get("oApartments")) + 1));
                    } else {
                        thirdFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(thirdFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 4){
                if(fourthFloor.get("floor") == null){
                    fourthFloor.put("floor", "4");
                    fourthFloor.put("totalApartments", "1");
                    fourthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    fourthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, fourthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(fourthFloor.get("totalApartments")) + 1;
                    fourthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        fourthFloor.put("availableApartments", String.valueOf(Integer.parseInt(fourthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        fourthFloor.put("oApartments", String.valueOf(Integer.parseInt(fourthFloor.get("oApartments")) + 1));
                    } else {
                        fourthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(fourthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 5){
                if(fifthFloor.get("floor") == null){
                    fifthFloor.put("floor", "5");
                    fifthFloor.put("totalApartments", "1");
                    fifthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    fifthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, fifthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(fifthFloor.get("totalApartments")) + 1;
                    fifthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        fifthFloor.put("availableApartments", String.valueOf(Integer.parseInt(fifthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        fifthFloor.put("oApartments", String.valueOf(Integer.parseInt(fifthFloor.get("oApartments")) + 1));
                    } else {
                        fifthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(fifthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 6){
                if(sixthFloor.get("floor") == null){
                    sixthFloor.put("floor", "6");
                    sixthFloor.put("totalApartments", "1");
                    sixthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    sixthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, sixthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(sixthFloor.get("totalApartments")) + 1;
                    sixthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        sixthFloor.put("availableApartments", String.valueOf(Integer.parseInt(sixthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        sixthFloor.put("oApartments", String.valueOf(Integer.parseInt(sixthFloor.get("oApartments")) + 1));
                    } else {
                        sixthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(sixthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 7){
                if(seventhFloor.get("floor") == null){
                    seventhFloor.put("floor", "7");
                    seventhFloor.put("totalApartments", "1");
                    seventhFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    seventhFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, seventhFloor);
                }else{
                    numberOfApartment = Integer.parseInt(seventhFloor.get("totalApartments")) + 1;
                    seventhFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        seventhFloor.put("availableApartments", String.valueOf(Integer.parseInt(seventhFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        seventhFloor.put("oApartments", String.valueOf(Integer.parseInt(seventhFloor.get("oApartments")) + 1));
                    } else {
                        seventhFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(seventhFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 8){
                if(eighthFloor.get("floor") == null){
                    eighthFloor.put("floor", "8");
                    eighthFloor.put("totalApartments", "1");
                    eighthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    eighthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, eighthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(eighthFloor.get("totalApartments")) + 1;
                    eighthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        eighthFloor.put("availableApartments", String.valueOf(Integer.parseInt(eighthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        eighthFloor.put("oApartments", String.valueOf(Integer.parseInt(eighthFloor.get("oApartments")) + 1));
                    } else {
                        eighthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(eighthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 9){
                if(ninthFloor.get("floor") == null){
                    ninthFloor.put("floor", "9");
                    ninthFloor.put("totalApartments", "1");
                    ninthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    ninthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, ninthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(ninthFloor.get("totalApartments")) + 1;
                    ninthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        ninthFloor.put("availableApartments", String.valueOf(Integer.parseInt(ninthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        ninthFloor.put("oApartments", String.valueOf(Integer.parseInt(ninthFloor.get("oApartments")) + 1));
                    } else {
                        ninthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(ninthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 10){
                if(tenthFloor.get("floor") == null){
                    tenthFloor.put("floor", "10");
                    tenthFloor.put("totalApartments", "1");
                    tenthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    tenthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, tenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(tenthFloor.get("totalApartments")) + 1;
                    tenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        tenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(tenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        tenthFloor.put("oApartments", String.valueOf(Integer.parseInt(tenthFloor.get("oApartments")) + 1));
                    } else {
                        tenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(tenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 11){
                if(eleventhFloor.get("floor") == null){
                    eleventhFloor.put("floor", "11");
                    eleventhFloor.put("totalApartments", "1");
                    eleventhFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    eleventhFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, eleventhFloor);
                }else{
                    numberOfApartment = Integer.parseInt(eleventhFloor.get("totalApartments")) + 1;
                    eleventhFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        eleventhFloor.put("availableApartments", String.valueOf(Integer.parseInt(eleventhFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        eleventhFloor.put("oApartments", String.valueOf(Integer.parseInt(eleventhFloor.get("oApartments")) + 1));
                    } else {
                        eleventhFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(eleventhFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 12){
                if(twelfthFloor.get("floor") == null){
                    twelfthFloor.put("floor", "12");
                    twelfthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, twelfthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(twelfthFloor.get("totalApartments")) + 1;
                    twelfthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        twelfthFloor.put("availableApartments", String.valueOf(Integer.parseInt(twelfthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        twelfthFloor.put("oApartments", String.valueOf(Integer.parseInt(twelfthFloor.get("oApartments")) + 1));
                    } else {
                        twelfthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(twelfthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 13){
                if(thirteenthFloor.get("floor") == null){
                    thirteenthFloor.put("floor", "13");
                    thirteenthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, thirteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(thirteenthFloor.get("totalApartments")) + 1;
                    thirteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        thirteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(thirteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        thirteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(thirteenthFloor.get("oApartments")) + 1));
                    } else {
                        thirteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(thirteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }else if(apartment.getFloor() == 14){
                if(fourteenthFloor.get("floor") == null){
                    fourteenthFloor.put("floor", "14");
                    fourteenthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, fourteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(fourteenthFloor.get("totalApartments")) + 1;
                    fourteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        fourteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(fourteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        fourteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(fourteenthFloor.get("oApartments")) + 1));
                    } else {
                        fourteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(fourteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 15){
                if(fifteenthFloor.get("floor") == null){
                    fifteenthFloor.put("floor", "15");
                    fifteenthFloor.put("totalApartments", "1");
                    fifteenthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    fifteenthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, fifteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(fifteenthFloor.get("totalApartments")) + 1;
                    fifteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        fifteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(fifteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        fifteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(fifteenthFloor.get("oApartments")) + 1));
                    } else {
                        fifteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(fifteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 16){
                if(sixteenthFloor.get("floor") == null){
                    sixteenthFloor.put("floor", "16");
                    sixteenthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, sixteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(sixteenthFloor.get("totalApartments")) + 1;
                    sixteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        sixteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(sixteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        sixteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(sixteenthFloor.get("oApartments")) + 1));
                    } else {
                        sixteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(sixteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 17){
                if(seventeenthFloor.get("floor") == null){
                    seventeenthFloor.put("floor", "17");
                    seventeenthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, seventeenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(seventeenthFloor.get("totalApartments")) + 1;
                    seventeenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        seventeenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(seventeenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        seventeenthFloor.put("oApartments", String.valueOf(Integer.parseInt(seventeenthFloor.get("oApartments")) + 1));
                    } else {
                        seventeenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(seventeenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 18){
                if(eighteenthFloor.get("floor") == null){
                    eighteenthFloor.put("floor", "18");
                    eighteenthFloor.put("totalApartments", "1");
                    eighteenthFloor.put("apartmentType", String.valueOf(apartment.getType()));
                    eighteenthFloor.put("apartmentState", String.valueOf(apartment.getState()));
                    updateAppsState(apartment, eighteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(eighteenthFloor.get("totalApartments")) + 1;
                    eighteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        eighteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(eighteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        eighteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(eighteenthFloor.get("oApartments")) + 1));
                    } else {
                        eighteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(eighteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 19){
                if(nineteenthFloor.get("floor") == null){
                    nineteenthFloor.put("floor", "19");
                    nineteenthFloor.put("totalApartments", "1");
                    updateAppsState(apartment, nineteenthFloor);
                }else{
                    numberOfApartment = Integer.parseInt(nineteenthFloor.get("totalApartments")) + 1;
                    nineteenthFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        nineteenthFloor.put("availableApartments", String.valueOf(Integer.parseInt(nineteenthFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        nineteenthFloor.put("oApartments", String.valueOf(Integer.parseInt(nineteenthFloor.get("oApartments")) + 1));
                    } else {
                        nineteenthFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(nineteenthFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }
            else if(apartment.getFloor() == 20){
                if(twentiethFloor.get("floor") == null){
                    twentiethFloor.put("floor", "20");
                    twentiethFloor.put("totalApartments", "1");
                    updateAppsState(apartment, twentiethFloor);
                }else{
                    numberOfApartment = Integer.parseInt(twentiethFloor.get("totalApartments")) + 1;
                    twentiethFloor.put("totalApartments", String.valueOf(numberOfApartment));
                    if (apartment.getState() == ApartmentState.AVAILABLE) {
                        twentiethFloor.put("availableApartments", String.valueOf(Integer.parseInt(twentiethFloor.get("availableApartments")) + 1));
                    } else if (apartment.getState() == ApartmentState.OCCUPIED) {
                        twentiethFloor.put("oApartments", String.valueOf(Integer.parseInt(twentiethFloor.get("oApartments")) + 1));
                    } else {
                        twentiethFloor.put("nAAvailableApartments", String.valueOf(Integer.parseInt(twentiethFloor.get("nAAvailableApartments")) + 1));
                    }
                }
            }

        }
        updateFloor(firstFloor, secondFloor, thirdFloor, fourthFloor, fifthFloor, sixthFloor, seventhFloor, eighthFloor, ninthFloor, tenthFloor);
        updateFloor(eleventhFloor, twelfthFloor, thirteenthFloor, fourteenthFloor, fifteenthFloor, sixteenthFloor, seventeenthFloor, eighteenthFloor, nineteenthFloor, twentiethFloor);

    }
    private void updateAppsState(Apartment apartment, ObservableMap<String, String> No) {
        if (apartment.getState() == ApartmentState.AVAILABLE) {
            No.put("availableApartments", "1");
            No.put("nAAvailableApartments", "0");
            No.put("oApartments", "0");
        } else if (apartment.getState() == ApartmentState.OCCUPIED) {
            No.put("availableApartments", "0");
            No.put("nAAvailableApartments", "0");
            No.put("oApartments", "1");
        } else {
            No.put("availableApartments", "0");
            No.put("nAAvailableApartments", "1");
            No.put("oApartments", "0");
        }
    }

    private void updateFloor(ObservableMap<String, String> firstFloor, ObservableMap<String, String> secondFloor, ObservableMap<String, String> thirdFloor, ObservableMap<String, String> fourthFloor, ObservableMap<String, String> fifthFloor, ObservableMap<String, String> sixthFloor, ObservableMap<String, String> seventhFloor, ObservableMap<String, String> eighthFloor, ObservableMap<String, String> ninthFloor, ObservableMap<String, String> tenthFloor) {
        floorList.add(firstFloor);
        floorList.add(secondFloor);
        floorList.add(thirdFloor);
        floorList.add(fourthFloor);
        floorList.add(fifthFloor);
        floorList.add(sixthFloor);
        floorList.add(seventhFloor);
        floorList.add(eighthFloor);
        floorList.add(ninthFloor);
        floorList.add(tenthFloor);
    }

    public void showFloorList(){
        updateFloorDetails();
        floorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("floor")));
        totalColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("totalApartments")));
        availableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("availableApartments")));
        nAvailableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("nAAvailableApartments")));
        occupiedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get("oApartments")));
        floorTableView.setItems(floorList);

    }
    public void initialize() throws IOException {
        showFloorList();
        apartmentTableView.getSelectionModel().clearSelection();
    }

    public void selectedFloor() {
        floorTableView.setOnMouseClicked(event -> {
            selectedFloor = floorTableView.getSelectionModel().getSelectedItem();
            showFloorDetail(Integer.parseInt(selectedFloor.get("floor")));
            handleClickedFloor();
        });
        backBtn.setOnMouseClicked(event -> {
            floorTableView.setVisible(true);
            apartmentTableView.setVisible(false);
            backBtn.setVisible(false);
            filterBtn.setVisible(false);

        });
    }

    private void showFloorDetail(int floor) {
        List<Apartment> floorApartments = new ArrayList<>();
        for(Apartment apartment : apartments){
            if(apartment.getFloor() == floor){
                floorApartments.add(apartment);
            }

        }
        ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(floorApartments);
        showApartments(apartmentObservableList);
    }

    public void handleClickedFloor(){
        floorTableView.setVisible(false);
        apartmentTableView.setVisible(true);
        backBtn.setVisible(true);
        filterBtn.setVisible(true);
    }
    public void toggleFilter(){
        if(filterBtn.visibleProperty().getValue()){
            filterBtn.setOnMouseClicked(event -> {
                dialogContainer.setVisible(true);
                dialogBox.setVisible(true);
            });
            List<MenuItem> typeItems = FXCollections.observableArrayList(studioItem, penthouseItem, duplexItem, triplexItem);
            List<MenuItem> stateItems = FXCollections.observableArrayList(availableItem, occupiedItem, reservedItem, maintainingItem);
            selectedType(typeItems);
            selectedState(stateItems);
            List<Apartment> selectedFloorForFilter = new ArrayList<>() ;
            for(Apartment apartment : apartments){
                if(apartment.getFloor() == Integer.parseInt(selectedFloor.get("floor"))){
                    selectedFloorForFilter.add(apartment);
                }
            }
            submitFilter.setOnMouseClicked(e -> {
                ApartmentFilter apartmentFilter = new ApartmentFilter(apartmentIDFilter, hostNameFilter, stateMenu, typeMenu, submitFilter, typeItems, stateItems, selectedFloorForFilter);
                List<Apartment> filteredApartment = apartmentFilter.handleFilter();
                if (filteredApartment.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Không tìm thấy kết quả");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy căn hộ " +apartmentIDFilter.getText());
                    alert.showAndWait();
                }
                else {
                    dialogContainer.setVisible(false);
                    dialogBox.setVisible(false);
                    apartmentIDFilter.clear();
                    hostNameFilter.clear();
                    stateMenu.setText("Choose State");
                    typeMenu.setText("Choose Type");
                    ObservableList<Apartment> apartmentObservableList = FXCollections.observableList(filteredApartment);
                    showApartments(apartmentObservableList);
                }
            });
        }
        if (closeDialogBtn.visibleProperty().getValue()){
            closeDialogBtn.setOnMouseClicked(event -> {
                dialogContainer.setVisible(false);
                dialogBox.setVisible(false);
            });
        }
    }
    public void selectedType(List<MenuItem> listItems){
        for (MenuItem selectedItems : listItems){
            selectedItems.setOnAction(event -> typeMenu.setText(selectedItems.getText()));
        }
    }
    public void selectedState(List<MenuItem> listItems){
        for (MenuItem selectedItems : listItems){
            selectedItems.setOnAction(event -> stateMenu.setText(selectedItems.getText()));
        }
    }
    public void showApartments(ObservableList<Apartment> index){
        apartmentIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        stateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getState()).asString());
        typeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getType()).asString());
        areaCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getArea()));
        stateCol.setCellFactory(column -> {
            return new TextFieldTableCell<Apartment, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null){
                        if(item.equals(ApartmentState.AVAILABLE.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("available-state");

                        }else if(item.equals(ApartmentState.OCCUPIED.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("occupied-state");

                        }else if(item.equals(ApartmentState.MAINTENANCE.toString())) {
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("maintenance-state");

                        }else if(item.equals(ApartmentState.RESERVED.toString())){
                            getStyleClass().add("state-apartment-design");
                            getStyleClass().add("reserved-state");


                        }
                    }
                }
            };
        });
        Callback<TableColumn<Apartment, String>, TableCell<Apartment, String>> cellFoctory = (TableColumn<Apartment, String> param) -> {
            final TableCell<Apartment, String> cell = new TableCell<Apartment, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);

                    } else {
                        Button editBtn = new Button("Edit");
                        Button deleteBtn = new Button("Delete");
                        editBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                                        + "-fx-border-width: 1px;"
                                        + "-fx-border-color: black;"
                                        + "-fx-border-radius: 14;"
                                        + "-fx-background-radius: 14;"
                        );
                        deleteBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                                        + "-fx-border-width: 1px;"
                                        + "-fx-border-color: black;"
                                        + "-fx-border-radius: 14;"
                                        + "-fx-background-radius: 14;"
                        );
                        editBtn.setOnMouseExited(e -> editBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                                        + "-fx-border-width: 1px;"
                                        + "-fx-border-color: black;"
                                        + "-fx-border-radius: 14;"
                                        + "-fx-background-radius: 14;"
                        ));
                        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #dcdcdc;"
                                + "-fx-border-width: 1px;"
                                + "-fx-border-color: black;"
                                + "-fx-border-radius: 14;"
                                + "-fx-background-radius: 14;"));
                        editBtn.setOnMouseClicked(e -> editBtn.setStyle("-fx-background-color: #b7b7b7;"
                                + "-fx-border-width: 1px;"
                                + "-fx-border-color: black;"
                                + "-fx-border-radius: 14;"
                                + "-fx-background-radius: 14;"));
                        deleteBtn.setOnMouseClicked(e -> deleteBtn.setStyle("-fx-background-color: #b7b7b7;"
                                + "-fx-border-width: 1px;"
                                + "-fx-border-color: black;"
                                + "-fx-border-radius: 14;"
                                + "-fx-background-radius: 14;"));
                        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-background-color: #ffff;"
                                        + "-fx-border-width: 1px;"
                                        + "-fx-border-color: black;"
                                        + "-fx-border-radius: 14;"
                                        + "-fx-background-radius: 14;"
                        ));
                        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #dcdcdc;"
                                + "-fx-border-width: 1px;"
                                + "-fx-border-color: black;"
                                + "-fx-border-radius: 14;"
                                + "-fx-background-radius: 14;"));
                        editBtn.setOnMouseClicked(e -> {
                            dialogContainer.setVisible(true);
                            apartmentInfoDialog.setVisible(true);
                            apartmentInfoClosebtn.setOnMouseClicked(e1 -> {
                                dialogContainer.setVisible(false);
                                apartmentInfoDialog.setVisible(false);
                            });
                            Apartment apartment = getTableView().getItems().get(getIndex());
                            apartmentInfoID.setText(apartment.getId());
                            stateInfo.setText(String.valueOf(apartment.getState()));
                            typeInfo.setText(String.valueOf(apartment.getType()));
                            ObservableList<Resident> residentObservableList = FXCollections.observableList(apartment.getResidents());
                            residentID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getId())));
                            phoneNumber.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));
                            lastName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
                            email.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
                            nationalID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNationalID()));
                            residentTableView.setItems(residentObservableList);
                            addResBtn.setOnMouseClicked(e2 -> {
                                MenuViewManager.switchView(MenuView.RESIDENT_FORM);
                            });
                        });
                        HBox managebtn = new HBox(editBtn, deleteBtn);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteBtn, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editBtn, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };

            return cell;
        };
        actionsCol.setCellFactory(cellFoctory);
        System.out.println("Size of ObservableList before setting to TableView: " + index.size());
        apartmentTableView.setItems(index);
        System.out.println("Size of ObservableList after setting to TableView: " + apartmentTableView.getItems().size());
    }
}

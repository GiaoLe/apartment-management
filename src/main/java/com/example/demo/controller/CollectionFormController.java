package com.example.demo.controller;

import com.example.demo.dao.Apartment;
import com.example.demo.dao.ApartmentCollection;
import com.example.demo.dao.Collection;
import com.example.demo.dao.CollectionType;
import com.example.demo.gui.MenuView;
import com.example.demo.gui.MenuViewManager;
import com.example.demo.repository.ApartmentCollectionRepository;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.HibernateUtility;
import com.example.demo.service.ApartmentCollectionService;
import com.example.demo.service.ApartmentService;
import com.example.demo.service.CollectionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class CollectionFormController {

    public TextField amountTextField;
    public ChoiceBox<CollectionType> collectionTypeChoiceBox;
    public Button submitButton;
    public TextArea descriptionTextArea;
    public TextField nameTextField;
    public TableColumn<Collection, Double> amountCol;
    public TableView<Collection> collectionTableView;
    public TableColumn<Collection, String> nameCol;
    public TableColumn<Collection, String> typeCol;
    public TableColumn<Collection, String> descriptionCol;
    public VBox collectionVBox;
    public VBox vbox = new VBox();
    public DatePicker datePicker = new DatePicker();
    @FXML
    public void initialize() {
        collectionTypeChoiceBox.getItems().addAll(CollectionType.values());
        updateCollectionTableView();
    }

    public void updateCollectionTableView() {
        List<Collection> collectionList = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Collection order by name", Collection.class)
                .getResultList());
        collectionTableView.setItems(FXCollections.observableList(collectionList));
        nameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        typeCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getType() == CollectionType.MANAGEMENT_FEE){
                return new SimpleObjectProperty<>("Phí quản lý");
            } else if(cellData.getValue().getType() == CollectionType.SERVICE_FEE){
                return new SimpleObjectProperty<>("Phí dịch vụ");
            } else {
                return new SimpleObjectProperty<>("Phí tình nguyện");
            }
        });
        amountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
        collectionTypeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(CollectionType.DONATION)){
                Label label = new Label("Deadline payment");
                label.getStyleClass().add("label-design");
                vbox.getChildren().add(label);
                datePicker.getStyleClass().add("input-design");
                vbox.getChildren().add(datePicker);
                collectionVBox.getChildren().add(vbox);
            } else {
                collectionVBox.getChildren().remove(vbox);
            }
        });
    }

    public void submitButtonOnAction() {
        List<Apartment> apartments = new ApartmentService(new ApartmentRepository()).findAll();
        Collection collection = Collection.builder()
                .name(nameTextField.getText())
                .type(collectionTypeChoiceBox.getValue())
                .description(descriptionTextArea.getText())
                .amount(Double.parseDouble(amountTextField.getText()))
                .build();

        CollectionService collectionService = new CollectionService(new CollectionRepository());
        collectionService.persist(collection);

        ApartmentCollectionService apartmentCollectionService = new ApartmentCollectionService(new ApartmentCollectionRepository());
        if (collectionTypeChoiceBox.getValue() != CollectionType.DONATION){

            for (Apartment apartment : apartments) {
                if (apartment.getHost() != null){
                    Date deadlinePayment = apartment.getHost().getMoveInDate();
                    if (deadlinePayment.getYear() + 1900 < LocalDate.now().getYear()){
                        LocalDate localDate = LocalDate.of(deadlinePayment.getYear() + 1900, deadlinePayment.getMonth() + 1, 30);
                        while(localDate.isBefore(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().plus(1), LocalDate.now().getDayOfMonth()))){
                            if ((30 - apartment.getHost().getMoveInDate().getDate()) > 5){
                                apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, Date.valueOf(localDate)));
                            }
                            localDate=localDate.plusMonths(1);
                        }
                    }else {
                        for (int j = deadlinePayment.getMonth() ; j < LocalDate.now().getMonthValue() ; j++) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, LocalDate.now().getYear());
                            calendar.set(Calendar.DAY_OF_MONTH, 30);
                            if (deadlinePayment.getMonth() == apartment.getHost().getMoveInDate().getMonth()){
                                calendar.set(Calendar.MONTH, j);
                                java.util.Date date = calendar.getTime();
                                if (30 - apartment.getHost().getMoveInDate().getDate() > 5){
                                    apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date));
                                }
                            }else {
                                calendar.set(Calendar.MONTH, j);
                                java.util.Date date = calendar.getTime();
                                apartmentCollectionService.persist(new ApartmentCollection(apartment, collection, date));
                            }
                        }
                    }
                }

            }
        }else {
            apartmentCollectionService.persist(new ApartmentCollection(null, collection, Date.valueOf(datePicker.getValue())));
        }

        updateCollectionTableView();
        MenuViewManager.switchView(MenuView.COLLECTION_LIST);
    }
}
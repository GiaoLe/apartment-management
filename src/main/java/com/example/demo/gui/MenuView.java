package com.example.demo.gui;

public enum MenuView {
    RESIDENT_LIST("resident-list.fxml"),

    RESIDENT_FORM("resident-form.fxml"),

    APARTMENT_LIST("apartment-list.fxml"),
    APARTMENT_FORM("apartment-form.fxml"),
    COLLECTION_LIST("collection-list.fxml"),
    COLLECTION_FORM("collection-form.fxml"),
    COLLECTION_REPORT("collection-report.fxml"),
    DASHBOARD("dashboard.fxml");

    private static final String FXML_PATH = "/fxml/";
    private final String fileName;


    MenuView(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return FXML_PATH + fileName;
    }
}

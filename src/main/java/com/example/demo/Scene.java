package com.example.demo;

public enum Scene {
    RESIDENT_LIST("resident-list.fxml"),

    RESIDENT_FORM("resident-form.fxml");

    private static final String FXML_PATH = "/fxml/";
    private final String fileName;


    Scene(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return FXML_PATH + fileName;
    }
}

package com.example.demo.gui;

public enum Scene {
    LOGIN("login.fxml"),

    MENU("menu.fxml");

    private static final String FXML_PATH = "/fxml/";
    private final String fileName;

    Scene(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return FXML_PATH + fileName;
    }

}

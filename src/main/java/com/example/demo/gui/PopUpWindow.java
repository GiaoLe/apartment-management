package com.example.demo.gui;

public enum PopUpWindow {

    RESIDENT_POP_UP_FORM("resident-pop-up-form.fxml"),
    APARTMENT_POP_UP_FORM("apartment-pop-up-form.fxml");

    private static final String FXML_PATH = "/fxml/";
    private final String fileName;

    PopUpWindow(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return FXML_PATH + fileName;
    }
}

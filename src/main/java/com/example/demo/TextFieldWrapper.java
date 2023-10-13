package com.example.demo;

import javafx.scene.control.TextField;

public class TextFieldWrapper {
    private final TextField textField;

    public TextFieldWrapper(TextField textField) {
        this.textField = textField;
    }

    public void setErrorMessage(String errorMessage) {
        textField.setStyle("-fx-border-color: red");
        textField.setPromptText(errorMessage);
    }

    public String getText() {
        return textField.getText();
    }
}

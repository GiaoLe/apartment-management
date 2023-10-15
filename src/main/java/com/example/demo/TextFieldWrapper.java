package com.example.demo;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TextFieldWrapper {
    private final TextField textField;
    private final Label errorLabel;

    public TextFieldWrapper(TextField textField, Label errorLabel) {
        this.textField = textField;
        this.errorLabel = errorLabel;
    }

    public void checkEmpty() {
        if (textField.getText().isEmpty()) {
            setEmptyError();
        } else {
            errorLabel.setText("");
        }
    }

    public boolean isEmpty() {
        return textField.getText().isEmpty();
    }

    public void setEmptyError() {
        setEmptyError("This field is required");
    }

    public void setEmptyError(String message) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(message);
    }
}

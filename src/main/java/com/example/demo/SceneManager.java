package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    @Setter
    private static Stage stage;

    private SceneManager() {
    }

    public static void switchScene(String fxml) {
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(DemoApplication.class.getResource(fxml)))));
            stage.setTitle("Apartment Manager");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

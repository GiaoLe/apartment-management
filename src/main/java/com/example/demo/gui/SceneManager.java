package com.example.demo.gui;

import com.example.demo.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage stage;

    public static void switchScene(Scene scene) {
        try {
            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(scene.getFileName())))));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
        configureStage(stage);
    }

    private static void configureStage(Stage stage) {
        stage.setTitle("Apartment Manager");
        stage.getIcons().add(new Image(String.valueOf(Main.class.getResource("/media/apartment-icon.png"))));
    }

}

package com.example.demo.gui;

import com.example.demo.DemoApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage stage;

    public static void switchScene(Scene scene) {
        try {
            stage.setFullScreen(true);
            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(DemoApplication.class.getResource(scene.getFileName())))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        SceneManager.stage = stage;
        stage.setTitle("Apartment Manager");
        stage.getIcons().add(new Image(String.valueOf(DemoApplication.class.getResource("/media/apartment-icon.png"))));
        stage.centerOnScreen();
        stage.setMaximized(true);
    }

}

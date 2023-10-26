package com.example.demo;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DemoApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
//        try {
//            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(DemoApplication.class.getResource(MenuView.MENU.getFileName())))));
//            stage.setTitle("Apartment Manager");
//            stage.getIcons().add(new Image(String.valueOf(DemoApplication.class.getResource("/media/apartment-icon.png"))));
//            stage.centerOnScreen();
//            stage.setMaximized(true);
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        SceneManager.setStage(stage);
        SceneManager.switchScene(Scene.LOGIN_FORM);
    }
}

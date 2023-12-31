package com.example.demo.gui;

import com.example.demo.Main;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import javafx.stage.StageStyle;

public class PopUpWindowManager {

    private static Stage stage;

    public static void openPopUpWindow(PopUpWindow popUpWindow) {
        stage = new Stage();
        try {
            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(popUpWindow.getFileName())))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setResizable(false);
        stage.setIconified(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.centerOnScreen();
        stage.showAndWait();
        MenuViewManager.refreshView();
    }

    public static void closeCurrentWindow() {
        stage.close();
    }
}

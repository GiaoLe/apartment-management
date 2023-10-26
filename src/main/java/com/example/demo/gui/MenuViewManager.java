package com.example.demo.gui;

import com.example.demo.DemoApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class MenuViewManager {

    @Setter
    private static BorderPane borderPane;

    private MenuViewManager() {
    }

    public static void switchView(MenuView menuView) {
        try {
            borderPane.setCenter(new FXMLLoader(Objects.requireNonNull(DemoApplication.class.getResource(menuView.getFileName()))).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

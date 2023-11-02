package com.example.demo.gui;

import com.example.demo.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class MenuViewManager {

    @Setter
    private static BorderPane borderPane;

    private MenuViewManager() {}

    public static Object switchView(MenuView menuView) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(menuView.getFileName())));
            borderPane.setCenter(loader.load());
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

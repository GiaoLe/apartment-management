package com.example.demo.gui;

import com.example.demo.DemoApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    @Setter
    private static BorderPane borderPane;

    private SceneManager() {
    }

    public static void switchScene(String fxml) {
        try {
            borderPane.setCenter(new FXMLLoader(Objects.requireNonNull(DemoApplication.class.getResource(fxml))).load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.demo;

import com.example.demo.gui.MenuView;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.switchScene(Scene.MENU);
    }
}

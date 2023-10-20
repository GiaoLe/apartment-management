package com.example.demo;

import javafx.application.Application;
import javafx.stage.Stage;

public class DemoApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.switchScene(Scene.LOGIN_FORM.getFileName());
    }
}

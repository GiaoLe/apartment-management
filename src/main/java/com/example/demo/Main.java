package com.example.demo;

import com.example.demo.dao.Admin;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.services.AdminService;
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
        AdminService adminService = new AdminService(new AdminRepository());
        adminService.merge(new Admin("admin", "admin"));
    }
}

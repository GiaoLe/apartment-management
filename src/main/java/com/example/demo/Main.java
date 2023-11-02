package com.example.demo;

import com.example.demo.dao.Admin;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
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
        //Create a sample admin account
        AdminService adminService = new AdminService(new AdminRepository());
        adminService.merge(new Admin("admin", "admin"));
    }
}

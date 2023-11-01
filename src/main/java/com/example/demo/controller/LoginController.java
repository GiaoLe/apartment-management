package com.example.demo.controller;

import com.example.demo.dao.Admin;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    public TextField userText;
    public PasswordField passwordText;

    private final AdminService adminService = new AdminService(new AdminRepository());

    public void loginButtonOnAction() {
        String userName = userText.getText();
        String password = passwordText.getText();
        Alert alert;
        if (userName.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
        } else {
            Admin admin = adminService.findByID(userName);
            if (admin == null || admin.getPassword().compareTo(passwordText.getText()) != 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE!");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username/password");
                alert.showAndWait();
            } else {
                SceneManager.switchScene(Scene.MENU);
            }
        }
    }
}

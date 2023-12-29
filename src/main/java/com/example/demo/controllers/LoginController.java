package com.example.demo.controllers;

import com.example.demo.dao.Admin;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.services.AdminService;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Getter;

public class LoginController {

    private final AdminService adminService = new AdminService(new AdminRepository());
    public TextField userNameTextField;
    public PasswordField passwordField;

    @Getter
    private static String currentUserID = "Admin";

    public void signInButtonOnAction() {
        String userName = userNameTextField.getText();
        String password = passwordField.getText();
        Alert alert;
        if (userName.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
        } else {
            Admin admin = adminService.findByID(userName);
            if (admin == null || admin.getPassword().compareTo(passwordField.getText()) != 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE!");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username/password");
                alert.showAndWait();
            } else {
                currentUserID = userName;
                SceneManager.switchScene(Scene.MENU);
            }
        }
    }
}

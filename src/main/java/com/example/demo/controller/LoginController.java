package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.Scene;
import com.example.demo.SceneManager;
import com.example.demo.dao.Admin;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    public Button loginButton;

    public PasswordField passwordField;

    public TextField userTextField;

    public void handleLogin() {
        String user = userTextField.getText();
        String password = passwordField.getText();
        Alert alert;
        if (user.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
        } else {
            Admin admin = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Admin where userName = :userName and password = :password", Admin.class)
                    .setParameter("userName", userTextField.getText())
                    .setParameter("password", passwordField.getText())
                    .uniqueResult());
            if (admin == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE!");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username/password");
                alert.showAndWait();
            } else {
                SceneManager.switchScene(Scene.MENU.getFileName());
            }
        }
    }
}

package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.gui.Scene;
import com.example.demo.gui.SceneManager;
import com.example.demo.dao.Admin;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    public Button loginButton;

    public PasswordField passwordText;

    public TextField userText;

    public void handleLogin() {
        String user = userText.getText();
        String password = passwordText.getText();
        Alert alert;
        if (user.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
        } else {
            Admin admin = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Admin where userName = :userName and password = :password", Admin.class)
                    .setParameter("userName", userText.getText())
                    .setParameter("password", passwordText.getText())
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

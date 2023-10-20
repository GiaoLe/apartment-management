package com.example.demo.controller;

import com.example.demo.HibernateUtility;
import com.example.demo.Scene;
import com.example.demo.SceneManager;
import com.example.demo.dao.Admin;
import com.example.demo.service.AdminService;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @javafx.fxml.FXML
    private Button loginBtn;

    @javafx.fxml.FXML
    private PasswordField passwordText;

    @javafx.fxml.FXML
    private TextField userText;
    private AdminService adminService;
    private Alert alert;
    public void handleLogin() {
        String user = userText.getText();
        String password = passwordText.getText();
        if(user.isEmpty() || password.isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR MESSAGE!");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
        }else{
            Admin admin = HibernateUtility.getSessionFactory().fromTransaction(session -> session.createQuery("from Admin where userName = :userName and password = :password", Admin.class)
                    .setParameter("userName", userText.getText())
                    .setParameter("password", passwordText.getText())
                    .uniqueResult());
            if(admin == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE!");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username/password");
                alert.showAndWait();
            }else{
                SceneManager.switchScene(Scene.MENU.getFileName());
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

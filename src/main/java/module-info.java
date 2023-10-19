module com.example.demo {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires lombok;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.hibernate.validator;

    opens com.example.demo to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, org.hibernate.validator;
    exports com.example.demo;
    exports com.example.demo.dao;
    opens com.example.demo.dao to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.controller;
    opens com.example.demo.controller to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.repository;
    opens com.example.demo.repository to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.service;
    opens com.example.demo.service to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
}
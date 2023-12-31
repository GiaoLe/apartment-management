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
    requires org.controlsfx.controls;
    requires org.apache.commons.collections4;

    opens com.example.demo to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, org.hibernate.validator;
    exports com.example.demo;
    exports com.example.demo.dao;
    opens com.example.demo.dao to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.controllers;
    opens com.example.demo.controllers to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.repositories;
    opens com.example.demo.repositories to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.services;
    opens com.example.demo.services to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
    exports com.example.demo.gui;
    opens com.example.demo.gui to jakarta.persistence, javafx.fxml, org.hibernate.orm.core, org.hibernate.validator;
}
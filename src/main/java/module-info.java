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
}
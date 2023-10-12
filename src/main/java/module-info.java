module com.example.demo {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires lombok;
    requires jakarta.persistence;

    opens com.example.demo to javafx.fxml, org.hibernate.orm.core;
    exports com.example.demo;
}
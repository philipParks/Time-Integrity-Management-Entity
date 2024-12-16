module com.TIME {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;


    opens com.TIME to javafx.fxml;
    exports com.TIME;
    /* NEEDED FOR CONTROLLERS */
    opens com.TIME.controller to javafx.fxml;
    exports com.TIME.controller;
    /* NEEDED FOR MODELS */
    opens com.TIME.model to javafx.fxml;
    exports com.TIME.model;
}
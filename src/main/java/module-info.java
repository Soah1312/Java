module com.farmstock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.farmstock;
    exports com.farmstock.controller;
    exports com.farmstock.model;

    opens com.farmstock.controller to javafx.fxml;
    opens com.farmstock.model to javafx.base;
}

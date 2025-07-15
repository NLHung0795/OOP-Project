module com.example.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.net.http;
    requires org.apache.commons.lang3;

    opens main to javafx.fxml;
    exports main;
    exports product;
    exports controller;
    opens controller to javafx.fxml;

    exports details;
    opens details to com.fasterxml.jackson.databind, javafx.fxml;
    opens product to com.fasterxml.jackson.databind, javafx.fxml;
}
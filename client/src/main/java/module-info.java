module com.example.civilization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    
    opens controllers to javafx.fxml, com.google.gson;
    opens models to javafx.fxml, com.google.gson, javafx.base;
    opens models.resources to javafx.fxml, javafx.base;
    opens models.improvements to javafx.fxml, javafx.base;
    opens netPackets to javafx.base, javafx.fxml, com.google.gson;
    opens terminalViews to javafx.fxml;
    opens views to javafx.fxml;
    opens views.controllers to javafx.fxml;
    opens menusEnumerations to javafx.fxml;
    opens utilities to javafx.fxml;
    exports views;
}
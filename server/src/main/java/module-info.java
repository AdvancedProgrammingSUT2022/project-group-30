module com.example.civilization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    
    opens controllers to javafx.fxml, com.google.gson;
    opens models to javafx.fxml, com.google.gson, javafx.base;
    opens models.resources to javafx.fxml, javafx.base, com.google.gson;
    opens models.buildings to com.google.gson;
    opens models.chat to com.google.gson;
    opens models.diplomacy to com.google.gson;
    opens models.interfaces to com.google.gson;
    opens models.technology to com.google.gson;
    opens models.units to com.google.gson;
    opens models.works to com.google.gson;
    opens models.improvements to javafx.fxml, javafx.base, com.google.gson;
    opens terminalViews to javafx.fxml;
    opens views to javafx.fxml;
    opens netPackets to javafx.fxml, javafx.base, com.google.gson;
    opens menusEnumerations to javafx.fxml, com.google.gson;
    opens utilities to javafx.fxml;
    exports views;
}
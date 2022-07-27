module com.example.civilization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires xstream;


    opens controllers to javafx.fxml, com.google.gson;
    opens models to javafx.fxml, com.google.gson, javafx.base, xstream;
    opens models.resources to javafx.fxml, javafx.base, xstream;
    opens models.improvements to javafx.fxml, javafx.base, xstream;
    opens models.diplomacy to xstream;
    opens models.technology to xstream;
    opens models.buildings to xstream;
    opens models.units to xstream;
    opens models.works to xstream;
    opens terminalViews to javafx.fxml;
    opens views to javafx.fxml;
    opens views.controllers to javafx.fxml;
    opens menusEnumerations to javafx.fxml;
    opens utilities to javafx.fxml;
    exports views;
}
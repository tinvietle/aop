module com.example {
    requires transitive javafx.media;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires javafx.base;

    opens com.example to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.capture to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.menu to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.game to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.misc to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.register to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.result to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.settings to javafx.fxml, com.fasterxml.jackson.databind;

    exports com.example;
    exports com.example.capture;
    exports com.example.menu;
    exports com.example.game;
    exports com.example.misc;
    exports com.example.register;
    exports com.example.result;
    exports com.example.settings;
}
module com.example {
    requires transitive javafx.media;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires com.fasterxml.jackson.databind;

    opens com.example to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example;
}
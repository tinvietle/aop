module com.example {
    requires javafx.media;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens com.example to javafx.fxml;
    exports com.example;
}
package com.example.misc;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class Utils {
    static public void closeProgram() {
        if (confirmExit()) {
            System.exit(0);
        }
    }

    static public Boolean confirmExit() {
        Alert alert = confirmBox("Exit", "Are you sure you want to exit?", "Press OK to exit the program.");
        styleDialog(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    static public Alert confirmBox(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleDialog(alert);
        return alert;
    }

    static private void styleDialog(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        alert.initStyle(StageStyle.TRANSPARENT);
        dialogPane.getStylesheets().add(
            Utils.class.getResource("/misc/style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }
}

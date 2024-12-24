package com.example.misc;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class Utils {
    static public void closeProgram() {
        if (confirmExit()) {
            System.out.println("Close Program");
            System.exit(0);
        }
    }

    static public Boolean confirmExit() {
        Alert alert = confirmBox("Exit", "Are you sure you want to exit?", "Press OK to exit the program.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    static public Alert confirmBox(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}

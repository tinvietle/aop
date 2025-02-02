/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.misc;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class Utils {
    static public void closeProgram() {
        /*
         * Closes the program after user confirmation.
         * 
         * Process:
         * - Shows confirmation dialog
         * - Exits system if confirmed
         */
        if (confirmExit()) {
            System.exit(0);
        }
    }

    static public Boolean confirmExit() {
        /*
         * Shows an exit confirmation dialog.
         * 
         * Returns:
         * - true if user confirms exit
         * - false if user cancels
         */
        Alert alert = confirmBox("Exit", "Are you sure you want to exit?", "Press OK to exit the program.");
        styleDialog(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    static public Alert confirmBox(String title, String header, String content) {
        /*
         * Creates a styled confirmation dialog box.
         * 
         * Parameters:
         * - title: Dialog window title
         * - header: Header text of dialog
         * - content: Main message content
         * 
         * Returns:
         * - Configured Alert object ready for display
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleDialog(alert);
        return alert;
    }

    static private void styleDialog(Alert alert) {
        /*
         * Applies custom styling to an Alert dialog.
         * 
         * Parameters:
         * - alert: Alert object to style
         * 
         * Features:
         * - Sets transparent stage style
         * - Applies CSS styling
         * - Adds dialog-pane style class
         */
        DialogPane dialogPane = alert.getDialogPane();
        alert.initStyle(StageStyle.TRANSPARENT);
        dialogPane.getStylesheets().add(
            Utils.class.getResource("/misc/style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }
}
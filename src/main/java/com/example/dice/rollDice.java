package com.example.dice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class rollDice extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("diceLayout.fxml"));
        Parent root = loader.load();
        DiceController controller = loader.getController();
        
        // Set the callback for when rolling is complete
        controller.setOnRollComplete(() -> onRollComplete(controller));

        primaryStage.setTitle("Dice Roller");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void onRollComplete(DiceController controller) {
        String result = controller.getResult();
        System.out.println("Final kept dices: " + result);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
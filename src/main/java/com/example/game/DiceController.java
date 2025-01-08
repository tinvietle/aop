package com.example.game;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DiceController {

    private static final int ROLL_ITERATIONS = 15;
    private static final int ROLL_DELAY_MS = 50;

    private final Random random = new Random();
    private final List<String> diceSides = new ArrayList<>();
    private final List<ImageView> keepDice = new ArrayList<>();
    private int numKeptDice = 0;
    private int totalDice = 7;
    private boolean firstRoll = true;
    private StringBuilder result = new StringBuilder();  
    private Runnable onRollComplete; // Callback for roll completion

    @FXML
    private Pane Pane;
    @FXML
    private ImageView dice1, dice2, dice3, dice4, dice5, dice6, dice7;

    @FXML
    private Button rollButton, endButton;

    @FXML
    public GameController gameController;

    @FXML
    void initialize() {

        // Bind the width and height of the dice images to the parent pane
        dice1.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice1.fitHeightProperty().bind(dice1.fitWidthProperty());
        dice2.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice2.fitHeightProperty().bind(dice2.fitWidthProperty());
        dice3.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice3.fitHeightProperty().bind(dice3.fitWidthProperty());
        dice4.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice4.fitHeightProperty().bind(dice4.fitWidthProperty());
        dice5.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice5.fitHeightProperty().bind(dice5.fitWidthProperty());
        dice6.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice6.fitHeightProperty().bind(dice6.fitWidthProperty());
        dice7.fitWidthProperty().bind(Pane.widthProperty().multiply(0.0625));
        dice7.fitHeightProperty().bind(dice7.fitWidthProperty());

        // Bind the buttons width to the parent pane
        rollButton.prefWidthProperty().bind(Pane.widthProperty().multiply(0.1));
        rollButton.prefHeightProperty().bind(rollButton.widthProperty().multiply(0.5));
        endButton.prefWidthProperty().bind(Pane.widthProperty().multiply(0.1));
        endButton.prefHeightProperty().bind(endButton.widthProperty().multiply(0.5));

        // Bind the layout of buttons and dices to the parent pane
        rollButton.layoutXProperty().bind(Pane.widthProperty().multiply(26.0 / 800.0));
        rollButton.layoutYProperty().bind(Pane.heightProperty().multiply(23.0 / 76.0));
        endButton.layoutXProperty().bind(Pane.widthProperty().multiply(110.0 / 800.0));
        endButton.layoutYProperty().bind(Pane.heightProperty().multiply(23.0 / 76.0));
        dice1.layoutXProperty().bind(Pane.widthProperty().multiply(208.0 / 800.0));
        dice1.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice2.layoutXProperty().bind(Pane.widthProperty().multiply(300.0 / 800.0));
        dice2.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice3.layoutXProperty().bind(Pane.widthProperty().multiply(392.0 / 800.0));
        dice3.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice4.layoutXProperty().bind(Pane.widthProperty().multiply(478.0 / 800.0));
        dice4.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice5.layoutXProperty().bind(Pane.widthProperty().multiply(558.0 / 800.0));
        dice5.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice6.layoutXProperty().bind(Pane.widthProperty().multiply(637.0 / 800.0));
        dice6.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        dice7.layoutXProperty().bind(Pane.widthProperty().multiply(714.0 / 800.0));
        dice7.layoutYProperty().bind(Pane.heightProperty().multiply(13.0 / 76.0));
        


        try {
            File dir = new File(getClass().getResource("/com/example/assets/balls").toURI());
            File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    diceSides.add(file.toURI().toString());
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        initializeDiceClickListeners();
        endButton.setDisable(false);
    }

    @FXML
    void rollHandler(ActionEvent event) {
        rollButton.setDisable(true);
        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        // Update kept dice faces at the start
        for (int i = 0; i < numKeptDice; i++) {
            // Get the image of keep dices and update that images to dices number 1 -> numKeptDice
            ImageView diceImage = diceImages.get(i);
            diceImage.setImage(keepDice.get(i).getImage());
            diceImage.setOpacity(0.5); // Mark as kept
        }

        // Update the keepDice list with the new dice images
        keepDice.clear();
        keepDice.addAll(diceImages.subList(0, numKeptDice));

        // Roll remaining dice (totalDice - numKeptDice - 1)
        // if first roll, roll all dice
        int remainingDice = firstRoll ? totalDice : totalDice - numKeptDice;

        CountDownLatch latch = new CountDownLatch(remainingDice);

        for (int i = numKeptDice; i < 7; i++) {
            // Set the opacity of the dice to 1.0
            diceImages.get(i).setOpacity(1.0);
            ImageView diceImage = diceImages.get(i);
            if (i < numKeptDice + remainingDice){
                new Thread(() -> {
                    rollEachDice(diceImage);
                    latch.countDown();
                }).start();
            } else {
                // set dice image to null
                // diceImage.setImage(null);
                diceImage.setOpacity(0.0);
            }
        }

        new Thread(() -> {
            try {
                latch.await();
                Platform.runLater(() -> {
                    rollButton.setDisable(false);
                    if (totalDice - numKeptDice <= 1) {
                        rollButton.setDisable(true);
                        // Set the opacity of the dice to 0.5
                        diceImages.get(numKeptDice).setOpacity(0.5);
                    }
                    totalDice--; // Reduce total dice available
                    
                    if (firstRoll) {
                        firstRoll = false;
                        gameController.enableAllPokemons();
                        disableButtons(true, true);
                        gameController.showInstruction("You have rolled the dice once. Please select the pokemons you want to keep before continuing.", 400, 100, 1, 0, 3);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void rollEachDice(ImageView dice) {
        for (int i = 0; i < ROLL_ITERATIONS; i++) {
            String imagePath = diceSides.get(random.nextInt(diceSides.size()));
            Platform.runLater(() -> dice.setImage(new Image(imagePath)));
            try {
                Thread.sleep(ROLL_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDiceClickListeners() {
        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        for (ImageView diceImage : diceImages) {
            diceImage.setOnMouseClicked(mouseEvent -> {
                if (firstRoll) {
                    return;
                }
                if (keepDice.contains(diceImage)) {
                    // Unkeep the dice
                    keepDice.remove(diceImage);
                    diceImage.setOpacity(1.0); // Reset opacity
                    numKeptDice--;
                } else {
                    // Keep the dice
                    keepDice.add(diceImage);
                    diceImage.setOpacity(0.5); // Mark as kept
                    numKeptDice++;
                }
            });
        }
    }

    @FXML
    void endTurn(ActionEvent event) {
        if (firstRoll) {
            return;
        }

        // Disable the roll button and end button
        rollButton.setDisable(true);
        endButton.setDisable(true);

        // Add the final kept dice to the result and ignore null dice images
        keepDice.clear();
        for (ImageView diceImage : List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7)) {
            if (diceImage.getOpacity() > 0) {
                keepDice.add(diceImage);
            }
        }

        // // Set the opacity to 1
        // for (ImageView diceImage : keepDice) {
        //     diceImage.setOpacity(1.0);
        // }

        // Use StringBuilder to concatenate the name of file of the dice images
        for (ImageView diceImage : keepDice) {
            // Get the name of the file of the dice image and ignore the file type
            String filename = diceImage.getImage().getUrl().substring(diceImage.getImage().getUrl().lastIndexOf("/") + 1, diceImage.getImage().getUrl().lastIndexOf("."));
            result.append(filename).append(", ");
        }

        // Remove the last comma and space
        result.delete(result.length() - 2, result.length());

        System.out.println("Result: " + result.toString());

        // Notify the roll completion
        if (onRollComplete != null) {
            onRollComplete.run();
        }
    }

    public String getResult(){
        return result.toString();
    }

    public void setOnRollComplete(Runnable onRollComplete) {
        this.onRollComplete = onRollComplete;
    }

    public void resetDice() {
        System.out.println("Resetting turn");

        // Reset the number of kept dice
        numKeptDice = 0;
        totalDice = 7;
        firstRoll = true;
        result = new StringBuilder();

        // Reset the opacity of the dice images
        for (ImageView diceImage : List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7)) {
            diceImage.setOpacity(1.0);
        }

        // Enable the roll button
        disableButtons(false, false);
    }

    public void disableButtons(boolean roll, boolean end){
        rollButton.setDisable(roll);
        endButton.setDisable(end);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}

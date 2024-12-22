package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

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
    private ImageView dice1, dice2, dice3, dice4, dice5, dice6, dice7;

    @FXML
    private Button rollButton, endButton;

    @FXML
    void initialize() {
        try {
            File dir = new File(getClass().getResource("/com/example/balls").toURI());
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
    void roll(ActionEvent event) {
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
        firstRoll = false;
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
                diceImage.setImage(null);
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
        // Disable the roll button and end button
        rollButton.setDisable(true);
        endButton.setDisable(true);

        // Add the final kept dice to the result and ignore null dice images
        keepDice.clear();
        for (ImageView diceImage : List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7)) {
            if (diceImage.getImage() != null) {
                keepDice.add(diceImage);
            }
        }

        // Set the opacity to 1
        for (ImageView diceImage : keepDice) {
            diceImage.setOpacity(1.0);
        }

        // Use StringBuilder to concatenate the name of file of the dice images
        for (ImageView diceImage : keepDice) {
            // Get the name of the file of the dice image and ignore the file type
            String filename = diceImage.getImage().getUrl().substring(diceImage.getImage().getUrl().lastIndexOf("/") + 1, diceImage.getImage().getUrl().lastIndexOf("."));
            result.append(filename).append(", ");
        }

        // Remove the last comma and space
        result.delete(result.length() - 2, result.length());

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
}

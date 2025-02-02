/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.example.misc.Line;
import com.example.misc.SoundManager;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/*
 * Controls the dice rolling mechanics and game flow.
 * This class manages:
 * - Dice rolling animations and logic
 * - Turn management
 * - Pokemon catching mechanics
 * - UI interaction and button states
 * - Score tracking
 */
public class DiceController {

    private static final int ROLL_ITERATIONS = 15;
    private static final int ROLL_DELAY_MS = 50;

    private final Random random = new Random();
    private final List<String> diceSides = new ArrayList<>();
    private final List<ImageView> keepDice = new ArrayList<>();
    private int numKeptDice = 0;
    private int totalDice = 7;
    private boolean firstRoll = true;
    private int remainingDice = 0;
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

        /*
         * Initializes the dice controller UI components.
         * 
         * Features:
         * - Binds dice images to responsive layout
         * - Configures button dimensions
         * - Sets up layout positions
         * - Loads dice face images
         * - Initializes click listeners
         */
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
        
        // Bind button font size to the parent pane
        rollButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rollButton.heightProperty().multiply(0.5), ";")); // Ensure text color is set
        endButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", endButton.heightProperty().multiply(0.5), ";")); // Ensure text color is set


        try {
            String path = "/com/example/assets/balls/%s.png";
            List<String> ballNames = List.of("1Pokeball", "2Pokeball", "3Pokeball", "GreatBall", "UltraBall", "MasterBall");
            for (String name: ballNames) {
                diceSides.add(getClass().getResource(String.format(path, name)).toExternalForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize dice click listeners
        initializeDiceClickListeners();
        endButton.setDisable(false);
    }

    @FXML
    void rollHandler(ActionEvent event) {
        /*
         * Handles the dice rolling action.
         * 
         * Features:
         * - Plays sound effect
         * - Manages button states
         * - Processes kept dice
         * - Validates Pokemon catching requirements
         * - Updates game state
         * - Manages first roll special cases
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        disableButtons(true, true);
        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        if (gameController.getTarget() != null) {
            // Retrieve the new kept dice images using for loop
            List<ImageView> newKeptDiceImages = new ArrayList<>();
            for (ImageView diceImage : diceImages) {
                if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null && !keepDice.contains(diceImage)) {
                    newKeptDiceImages.add(diceImage);
                }
            }
            // Calculate the new kept dice images total balls
            Map<String, Integer> ballsMap = new HashMap<>();
            ballsMap.put("Red", 0);
            ballsMap.put("Great", 0);
            ballsMap.put("Ultra", 0);
            ballsMap.put("Master", 0);
            for (ImageView diceImage : newKeptDiceImages) {
                if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null) {
                    String filename = diceImage.getImage().getUrl().substring(diceImage.getImage().getUrl().lastIndexOf("/") + 1, diceImage.getImage().getUrl().lastIndexOf("."));
                    switch (filename) {
                        case "1Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 1);
                            break;
                        case "2Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 2);
                            break;
                        case "3Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 3);
                            break;
                        case "GreatBall":
                            ballsMap.put("Great", ballsMap.get("Great") + 1);
                            break;
                        case "UltraBall":
                            ballsMap.put("Ultra", ballsMap.get("Ultra") + 1);
                            break;
                        case "MasterBall":
                            ballsMap.put("Master", ballsMap.get("Master") + 1);
                            break;
                        default:
                            break;
                    }
                }
            }

            List<Line> tempStatisfiedLines = new ArrayList<>();

            // Get all the lines that are satisfied by the kept balls
            for (Line line : gameController.getTarget().getLines()) {
                if (line.satisfied(ballsMap)) {
                    tempStatisfiedLines.add(line);
                }
            }

            // if tempStatisfiedRequirements is empty, raise error
            // and set the opacity of new kept dice images to 1.0
            if (tempStatisfiedLines.isEmpty() && !newKeptDiceImages.isEmpty()){
                GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "The kept balls do not satisfy any lines of the chosen Pokemon. Please try again.");
                for (ImageView diceImage : newKeptDiceImages){
                    diceImage.setOpacity(1.0);
                }
                disableButtons(false, false);
                return;
            }
            
            // Do not cost one dice if requirements are met
            if (!newKeptDiceImages.isEmpty()){
                totalDice++;
            }
            
            // if length of tempStatisfiedRequirements is more than 1, select requirements with the most total balls
            // and set listRequirementMap of that to true
            if (tempStatisfiedLines.size() > 0){    
                Line chosenLine = tempStatisfiedLines.get(0);
                if (tempStatisfiedLines.size() > 1){
                    for (Line line : tempStatisfiedLines){
                        if (line.isBigger(chosenLine)){
                            chosenLine = line;
                        }
                    }
                } 
                gameController.reduceTarget(chosenLine);
            }
        }


        // Count numKeptDice and update the keepDice list
        numKeptDice = 0;
        keepDice.clear();
        for (ImageView diceImage : diceImages) {
            if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null) {
                numKeptDice++;
            }
        }

        // Calculate the remaining dice
        remainingDice = firstRoll ? totalDice : totalDice - numKeptDice;

        // If remaining dice is less than 0, raise error
        if (remainingDice < 0) {
            disableButtons(true, false);
            return;
        } 
        else if (remainingDice == 0){
            // Retrieve all not null dice and set the opacity to 0.5
            for (ImageView diceImage : diceImages) {
                if (diceImage.getOpacity() != 0.0) {
                    diceImage.setOpacity(0.5);
                    keepDice.add(diceImage);
                }
            }
            disableButtons(true, false);
            return;
        }
        else {
            for (ImageView diceImage : diceImages) {
                // Normal case, add the dice to keepDice list if it opacity is 0.5
                if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null) {
                    keepDice.add(diceImage);
                }
            }
        }

        // Update kept dice faces at the start
        for (int i = 0; i < numKeptDice; i++) {
            // Get the image of keep dices and update that images to dices number 1 -> numKeptDice
            ImageView diceImage = diceImages.get(i);
            diceImage.setImage(keepDice.get(i).getImage());
            diceImage.setOpacity(0.5); // Mark as kept
        }

        // Reset the keepDice list
        keepDice.clear();
        keepDice.addAll(diceImages.subList(0, numKeptDice));

        // Create a CountDownLatch to synchronize dice rolling threads
        CountDownLatch latch = new CountDownLatch(remainingDice);

        // Roll the remaining dice asynchronously
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
                diceImage.setOpacity(0.0);
            }
        }

        // Wait for all dice to finish rolling before enabling buttons
        new Thread(() -> {
            try {
                latch.await();
                Platform.runLater(() -> {   
                    disableButtons(false, false);              
                    if (firstRoll) {
                        firstRoll = false;
                        // disableButtons(true, true);
                        boolean isInstruction = gameController.showInstruction("You can start catching a Pokemon by filling one line per roll, or remove one dice and reroll.", 400, 100, 1, 0, 3000);
                        if (!isInstruction) {
                            gameController.disableAllPokemons(false);
                        } else {
                            GameUtils.delay(3000, () -> {
                                gameController.disableAllPokemons(false);;
                            });
                        }
                    } 
                    
                    if (totalDice - numKeptDice <= 1) {
                        disableButtons(true, false);
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
        /*
         * Animates the rolling of a single die.
         * 
         * Parameters:
         * - dice: The ImageView representing the die to animate
         * 
         * Implementation:
         * - Randomly cycles through dice faces
         * - Controls animation timing
         * - Updates dice image
         */
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
        /*
         * Sets up mouse click handlers for dice selection.
         * 
         * Features:
         * - Toggles dice selection state
         * - Validates selection rules
         * - Handles kept dice restrictions
         * - Shows error alerts for invalid actions
         */
        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        for (ImageView diceImage : diceImages) {
            diceImage.setOnMouseClicked(mouseEvent -> {
                // If the dice is kept, raise Error since cannot unkeep the dice
                if (keepDice.contains(diceImage) && diceImage.getOpacity() == 0.5){
                    GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "This dice is already kept. You cannot unkeep it.");
                    return;
                }
                // Toggle the opacity of the dice image
                if (gameController.getTarget() != null) {
                    diceImage.setOpacity(diceImage.getOpacity() == 1.0 ? 0.5 : 1.0);
                } else {
                    // Show alert that it is not possible to choose Pokeball, only reroll or select Pokemon
                    GameUtils.showAlert(Alert.AlertType.WARNING, "Invalid Pick", "You cannot choose the Pokeball. Please select a Pokemon to catch.");
                }
            });
        }
    }

    @FXML
    void endTurn(ActionEvent event) {
        /*
         * Processes the end of a player's turn.
         * 
         * Features:
         * - Validates final dice selections
         * - Updates Pokemon catch progress
         * - Records turn results
         * - Manages button states
         * - Triggers completion callback
         */
        SoundManager.getInstance().playSFX("/com/example/assets/soundeffect/button.wav");
        if (firstRoll) {
            return;
        }

        // Disable the roll button and end button
        rollButton.setDisable(true);
        endButton.setDisable(true);

        // Retrieve the picked dices before ending the turn
        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        if (gameController.getTarget() != null) {
            // Retrieve the new kept dice images using for loop
            List<ImageView> newKeptDiceImages = new ArrayList<>();
            for (ImageView diceImage : diceImages) {
                if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null && !keepDice.contains(diceImage)) {
                    newKeptDiceImages.add(diceImage);
                }
            }
            // Calculate the new kept dice images total balls
            Map<String, Integer> ballsMap = new HashMap<>();
            ballsMap.put("Red", 0);
            ballsMap.put("Great", 0);
            ballsMap.put("Ultra", 0);
            ballsMap.put("Master", 0);
            for (ImageView diceImage : newKeptDiceImages) {
                if (diceImage.getOpacity() == 0.5 && diceImage.getImage() != null) {
                    String filename = diceImage.getImage().getUrl().substring(diceImage.getImage().getUrl().lastIndexOf("/") + 1, diceImage.getImage().getUrl().lastIndexOf("."));
                    switch (filename) {
                        case "1Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 1);
                            break;
                        case "2Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 2);
                            break;
                        case "3Pokeball":
                            ballsMap.put("Red", ballsMap.get("Red") + 3);
                            break;
                        case "GreatBall":
                            ballsMap.put("Great", ballsMap.get("Great") + 1);
                            break;
                        case "UltraBall":
                            ballsMap.put("Ultra", ballsMap.get("Ultra") + 1);
                            break;
                        case "MasterBall":
                            ballsMap.put("Master", ballsMap.get("Master") + 1);
                            break;
                        default:
                            break;
                    }
                }
            }

            List<Line> tempStatisfiedLines = new ArrayList<>();

            for (Line line : gameController.getTarget().getLines()) {
                if (line.satisfied(ballsMap)) {
                    tempStatisfiedLines.add(line);
                }
            }

            if (tempStatisfiedLines.size() > 0){    
                Line chosenLine = tempStatisfiedLines.get(0);
                if (tempStatisfiedLines.size() > 1){
                    for (Line line : tempStatisfiedLines){
                        if (line.isBigger(chosenLine)){
                            chosenLine = line;
                        }
                    }
                } 
                gameController.reduceTarget(chosenLine);
            }
        }

        // Add the final kept dice to the result and ignore null dice images
        keepDice.clear();
        for (ImageView diceImage : List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7)) {
            if (diceImage.getOpacity() > 0) {
                keepDice.add(diceImage);
            }
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
        /*
         * Returns the formatted result of the current roll.
         * 
         * Returns:
         * - String containing the names of kept dice faces
         */
        return result.toString();
    }

    public void setOnRollComplete(Runnable onRollComplete) {
        /*
         * Sets the callback for roll completion.
         * 
         * Parameters:
         * - onRollComplete: Runnable to execute when rolling finishes
         */
        this.onRollComplete = onRollComplete;
    }

    public void resetDice() {
        /*
         * Resets the dice controller to initial state.
         * 
         * Actions:
         * - Resets counters and flags
         * - Clears results
         * - Resets dice appearance
         * - Enables buttons
         */
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
        /*
         * Controls the enabled state of roll and end buttons.
         * 
         * Parameters:
         * - roll: Whether to disable the roll button
         * - end: Whether to disable the end button
         */
        rollButton.setDisable(roll);
        endButton.setDisable(end);
    }

    public void setGameController(GameController gameController) {
        /*
         * Sets the reference to the main game controller.
         * 
         * Parameters:
         * - gameController: The GameController instance to link
         */
        this.gameController = gameController;
    }
}

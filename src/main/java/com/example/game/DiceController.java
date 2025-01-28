package com.example.game;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.example.misc.Line;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private int remainingDice = 0;
    private StringBuilder result = new StringBuilder();  
    private Runnable onRollComplete; // Callback for roll completion
    // private Pokemon chosenPokemon;
    private Map<String, Boolean> listRequirementsMap;
    private List<String> requirementList;

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
        
        // Bind button font size to the parent pane
        rollButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", rollButton.heightProperty().multiply(0.5), ";")); // Ensure text color is set
        endButton.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", endButton.heightProperty().multiply(0.5), ";")); // Ensure text color is set


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

            for (Line line : gameController.getTarget().getLines()) {
                if (line.satisfied(ballsMap)) {
                    tempStatisfiedLines.add(line);
                }
            }

            // // Count types of balls
            // int totalTypes = 0;
            // for (String ballType : ballsMap.keySet()){
            //     if (ballsMap.get(ballType) > 0){
            //         totalTypes++;
            //         System.out.println("Ball type: " + ballType + " Count: " + ballsMap.get(ballType));
            //     }
            // }
            // System.out.println("Total types: " + totalTypes);
            // // Check if the new kept dice images satisfy the requirements of the chosen pokemon
            // List<String> tempStatisfiedRequirements = new ArrayList<>();
            // // Loop through requirements
            // // print the requirements list map
            // System.out.println("Requirements map: " + listRequirementsMap);
            // for (String requirement : requirementList){
            //     // If the requirement is not satisfied
            //     if (!listRequirementsMap.get(requirement)){
            //         // Split the string to get requirements
            //         String[] requirementSplit = requirement.split(" ");
            //         int length = requirementSplit.length;
            //         // Check if the length of the requirement is equal to the total types of balls kept
            //         if (length == totalTypes){
            //             boolean isSatisfied = false;
            //             // Loop through the each small requirement
            //             for (int i = 0; i < length; i++){
            //                 // Split the requirement to get the ball type and the count
            //                 String[] parts = requirementSplit[i].split(":");
            //                 String ballType = parts[0];
            //                 int ballCount = Integer.parseInt(parts[1]);
            //                 if (ballsMap.get(ballType) == ballCount){
            //                     isSatisfied = true;
            //                 } else {
            //                     isSatisfied = false;
            //                     break;
            //                 }
            //             }
            //             if (isSatisfied){
            //                 tempStatisfiedRequirements.add(requirement);
            //             }
            //         }
            //     }
            // }
            // if tempStatisfiedRequirements is empty, raise error
            // and set the opacity of new kept dice images to 1.0
            System.out.println("Temp satisfied requirements: " + tempStatisfiedLines);
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
                    // int maxTotalBalls = 0;
                    // String maxRequirement = "";
                    // for (String requirement : tempStatisfiedRequirements){
                    //     String[] requirementSplit = requirement.split(" ");
                    //     int totalBalls = 0;
                    //     for (int i = 0; i < requirementSplit.length; i++){
                    //         String[] parts = requirementSplit[i].split(":");
                    //         int ballCount = Integer.parseInt(parts[1]);
                    //         totalBalls += ballCount;
                    //     }
                    //     if (totalBalls > maxTotalBalls){
                    //         maxTotalBalls = totalBalls;
                    //         maxRequirement = requirement;
                    //     }
                    // }
                    // // Set the listRequirementMap of the maxRequirement to true
                    // listRequirementsMap.put(maxRequirement, true);
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

        remainingDice = firstRoll ? totalDice : totalDice - numKeptDice;

        if (remainingDice < 0) {
            disableButtons(true, false);
            return;
        } 
        else if (remainingDice == 0){
            System.out.println("Triggering end by remaining dice");
            System.out.println("Total dice: " + totalDice);
            System.out.println("Num kept dice: " + numKeptDice);
            System.out.println("Remaining dice: " + remainingDice);
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
                        System.out.println("Triggering end turn");
                        System.out.println("Total dice: " + totalDice);
                        System.out.println("Num kept dice: " + numKeptDice);
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

                

                // if (keepDice.contains(diceImage)) {
                //     // Unkeep the dice
                //     keepDice.remove(diceImage);
                //     diceImage.setOpacity(1.0); // Reset opacity
                //     numKeptDice--;
                // } else {
                //     // Keep the dice
                //     keepDice.add(diceImage);
                //     diceImage.setOpacity(0.5); // Mark as kept
                //     numKeptDice++;
                // }
            });
        }
    }

    @FXML
    void endTurn(ActionEvent event) {
        if (firstRoll) {
            return;
        }

        // if (gameController.getChosenPokemon() == null) {
        //     Alert alert = Utils.confirmBox("You have not chosen a Pokemon", "You have not chosen a Pokemon", "Press OK if you want to end turn.");
        //     Optional<ButtonType> result = alert.showAndWait();
        //     boolean confirm = result.isPresent() && result.get() == ButtonType.OK;
        //     if (!confirm) return;
        // }

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
    // public void setChosenPokemon(Pokemon chosenPokemon) {
    //     this.chosenPokemon = chosenPokemon;
    //     if (chosenPokemon == null) {
    //         return;
    //     }
    //     // Set the requirements of the chosen pokemon and map each requirement to false
    //     Requirement requirements = chosenPokemon.getRequirement();
    //     listRequirementsMap = new HashMap<>();
    //     requirementList = new ArrayList<>();
        
    //     // Assuming Pokeball has a method to get all requirements as a list
    //     for (String requirement : requirements.getAllRequirements()) {
    //         listRequirementsMap.put(requirement, false);
    //         requirementList.add(requirement);
    //     }
    // }
}

package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceController {

    Random random = new Random();

    @FXML
    private ImageView dice1;

    @FXML
    private ImageView dice2;

    @FXML
    private ImageView dice3;

    @FXML
    private ImageView dice4;

    @FXML
    private ImageView dice5;

    @FXML
    private ImageView dice6;

    @FXML
    private ImageView dice7;

    @FXML
    private Button rollButton;

    private List<String> diceSides = new ArrayList<>();

    @FXML
    void initialize() {
        File dir = new File("src/main/java/com/example/balls");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
        if (files != null) {
            for (File file : files) {
                diceSides.add(file.getName());
            }
        }
    }

    private String rollDie() {
        return diceSides.get(random.nextInt(diceSides.size()));
    }

    @FXML
    void roll(ActionEvent event) {
        rollButton.setDisable(true);

        List<ImageView> diceImages = List.of(dice1, dice2, dice3, dice4, dice5, dice6, dice7);

        for (ImageView diceImage : diceImages) {
            new Thread(() -> rollEachDice(diceImage)).start();
        }

        rollButton.setDisable(false);
    }

    private void rollEachDice(ImageView dice) {
        File dir = new File("src/main/java/com/example/balls");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
        if (files != null && files.length > 0) {
            for (int i = 0; i < 15; i++) {
                File file = files[random.nextInt(files.length)];
                dice.setImage(new Image(file.toURI().toString()));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
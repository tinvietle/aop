package com.example.misc;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import com.fasterxml.jackson.annotation.JsonCreator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    @JsonProperty("name")
    String name;

    @JsonProperty("score")
    int score;

    @JsonProperty("requirements")
    Map<String, List<Integer>> requirementsMap;

    @JsonProperty("description")
    String description;

    boolean owned;
    Player owner;
    String group;

    Pokeball requirements;

    public Pokemon() {}

    @JsonCreator
    public Pokemon(
        @JsonProperty("name") String name,
        @JsonProperty("score") int score,
        @JsonProperty("requirements") Map<String, List<Integer>> requirementsMap,
        @JsonProperty("description") String description
    ) {
        this.name = name;
        this.score = score;
        this.description = description;
        this.requirementsMap = requirementsMap;
        this.requirements = mapToPokeball(requirementsMap); // Convert the map to a Pokeball object
    }

    private Pokeball mapToPokeball(Map<String, List<Integer>> requirementsMap) {
        Pokeball pokeball = new Pokeball();
        for (Map.Entry<String, List<Integer>> entry : requirementsMap.entrySet()) {
            switch (entry.getKey()) {
                case "red ball":
                    pokeball.red = entry.getValue();
                    break;
                case "great ball":
                    pokeball.great = entry.getValue().get(0);
                    break;
                case "ultra ball":
                    pokeball.ultra = entry.getValue().get(0);
                    break;
                case "master ball":
                    pokeball.master = entry.getValue().get(0);
                    break;
            }
        }
        return pokeball;
    }

    @Override
    public String toString() {
        return  "Name: " + name + '\n' +
                "Score: " + score + '\n' +
                "Requirements: " + requirements.toString() + '\n' +
                "Description: " + description + '\n';
    }

    public TextFlow getStyledTooltipContent(BorderPane root) {
        // Create Text nodes without static styles
        Text leftCorner = new Text("<");
        leftCorner.getStyleClass().add("text-name");

        Text nameText = new Text(name.toUpperCase());
        nameText.getStyleClass().add("text-name");

        Text rightCorner = new Text(">");
        rightCorner.getStyleClass().add("text-name");

        Text scoreText = new Text("\nScore: " + score );
        scoreText.getStyleClass().add("text-info");

        Text requirementsText = new Text("\nRequirements: \n" + requirements.toString());
        requirementsText.getStyleClass().add("text-info");

        Text descriptionText = new Text("\nDescription: " + description);
        descriptionText.getStyleClass().add("text-info");

        // Bind font size to the scene size
        nameText.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("Pocket Monk", FontWeight.BOLD, root.getWidth() / 50), // Adjust the divisor for scaling
            root.widthProperty()
        ));

        leftCorner.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 120),
            root.widthProperty()
        ));

        rightCorner.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 120),
            root.widthProperty()
        ));

        scoreText.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 100),
            root.widthProperty()
        ));

        requirementsText.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 100),
            root.widthProperty()
        ));

        descriptionText.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 100),
            root.widthProperty()
        ));

        // Wrap nameText, left/rightCorner in a centered HBox
        TextFlow nameContainer = new TextFlow(leftCorner, nameText, rightCorner);
        nameContainer.setTextAlignment(TextAlignment.CENTER);
        nameContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25));

        // Combine the centered name and other Text nodes into a TextFlow
        TextFlow textFlow = new TextFlow(nameContainer, scoreText, requirementsText, descriptionText);
        textFlow.getStyleClass().add("text-flow");

        // Bind textflow padding to root
        textFlow.paddingProperty().bind(Bindings.createObjectBinding(
            () -> new Insets(root.getHeight() * 0.01, root.getWidth() * 0.01, root.getHeight() * 0.01, root.getWidth() * 0.01),
            root.widthProperty(),
            root.heightProperty()
        ));

        String css = this.getClass().getResource("/com/example/game/style.css").toExternalForm();
        textFlow.getStylesheets().add(css);

        return textFlow;
    }


    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }

    public boolean getOwned() {
        return owned;
    }

    public Player getOwner() {
        return owner;
    }

    public String getGroup() {
        return group;
    }

    public Pokeball getRequirements() {
        return requirements;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void updateOwner(Player owner) {
        this.owner = owner;
        if (!this.owned) {
            this.owned = true;
            requirements.addMasterBall();
        }
        owner.addPokemon(this);
    }
}

/**
 * OOP Java Project WiSe 2024/2025
 * Age of Pokemon: A Pokemon-themed strategy game from Age of War
 * @author Viet Tin Le - 1585762
 * @author That Nhat Minh Ton - 1588341
 * @author Tri An Yamashita - 1590012
 * @version 1.0 - 2025-02-01
 */
package com.example.misc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
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
    @JsonProperty("group")
    String group;
    @JsonProperty("requirements")
    List<List<Integer>> requirementMap;
    @JsonProperty("description")
    String description;

    boolean owned;
    Player owner;
    Player groupOwner;
    Requirement requirement;
    int groupScore;

    public Pokemon() {}

    @JsonCreator
    public Pokemon(
        @JsonProperty("name") String name,
        @JsonProperty("score") int score,
        @JsonProperty("group") String group,
        @JsonProperty("requirements") List<List<Integer>> requirementMap,
        @JsonProperty("description") String description
    ) {
        /*
         * Creates a new Pokemon with specified attributes.
         * 
         * Parameters:
         * - name: Pokemon's name
         * - score: Points value of the Pokemon
         * - group: Group identifier
         * - requirementMap: List of catching requirements
         * - description: Pokemon's description text
         */
        this.name = name;
        this.score = score;
        this.group = group;
        this.description = description;
        this.requirementMap = requirementMap;
        this.requirement = mapToPokeball(requirementMap); // Convert the map to a Pokeball object
        this.owned = false;
        this.owner = null;
        this.groupOwner = null;
    }

    private Requirement mapToPokeball(List<List<Integer>> requirementMap) {
        /*
         * Converts requirement map to Pokeball requirement object.
         * 
         * Parameters:
         * - requirementMap: List of integer lists defining requirements
         * 
         * Returns:
         * - Requirement object containing all Pokeball lines
         */
        Requirement requirement = new Requirement();
        for (List<Integer> line : requirementMap) {
            Line pokeball = new Line(line);

            requirement.addLine(pokeball);
        }
        return requirement;
        }

    @Override
    public String toString() {
        /*
         * Creates a string representation of the Pokemon.
         * Includes group ownership, score, requirements and description.
         * 
         * Returns:
         * - Formatted string with Pokemon information
         */
        if (groupOwner != null) {
            return  "\nScore: " + score + '\n' +
                    "Group Score: " + groupScore + '\n' + 
                    "Group " + this.group + " is already owned by Player " + groupOwner.getName();
        }
        if (owned) {
            return  "\nScore: " + score + '\n' +
                    "Requirement: \n" + requirement.toString()  +
                    "Description: " + description + '\n' + '\n' +
                    "Owned by: " + owner.getName();
        }
        return  "\nScore: " + score + '\n' +
                "Requirement: \n" + requirement.toString()  +
                "Description: " + description ;
    }

    public TextFlow getStyledTooltipContent(BorderPane root) {
        /*
         * Creates a styled tooltip for Pokemon information display.
         * 
         * Parameters:
         * - root: BorderPane for size binding reference
         * 
         * Returns:
         * - TextFlow containing styled Pokemon information
         * 
         * Features:
         * - Responsive font sizing
         * - Custom styling
         * - Centered layout
         */
        // Create Text nodes without static styles
        Text leftCorner = new Text("<");
        leftCorner.getStyleClass().add("text-name");

        Text nameText = new Text(name.toUpperCase());
        nameText.getStyleClass().add("text-name");

        Text rightCorner = new Text(">");
        rightCorner.getStyleClass().add("text-name");

        Text descriptionText = new Text(this.toString());
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

        descriptionText.fontProperty().bind(Bindings.createObjectBinding(
            () -> Font.font("System", FontWeight.BOLD, root.getWidth() / 100),
            root.widthProperty()
        ));

        // Wrap nameText, left/rightCorner in a centered HBox
        TextFlow nameContainer = new TextFlow(leftCorner, nameText, rightCorner);
        nameContainer.setTextAlignment(TextAlignment.CENTER);
        nameContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.25));        

        // Combine the centered name and other Text nodes into a TextFlow
        TextFlow textFlow = new TextFlow(nameContainer, descriptionText);
        textFlow.prefWidthProperty().bind(root.widthProperty().multiply(0.25));
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
        /*
         * Gets the Pokemon's name.
         * 
         * Returns:
         * - String name of the Pokemon
         */
        return name;
    }

    public String getDescription() {
        /*
         * Gets the Pokemon's description.
         * 
         * Returns:
         * - String description of the Pokemon
         */
        return description;
    }

    public int getScore() {
        /*
         * Gets the Pokemon's score.
         * 
         * Returns:
         * - int score of the Pokemon
         */
        return score;
    }

    public boolean getOwned() {
        /*
         * Checks if the Pokemon is owned.
         * 
         * Returns:
         * - boolean indicating ownership status
         */
        return owned;
    }

    public Player getOwner() {
        /*
         * Gets the owner of the Pokemon.
         * 
         * Returns:
         * - Player who owns the Pokemon
         */
        return owner;
    }

    public String getGroup() {
        /*
         * Gets the group of the Pokemon.
         * 
         * Returns:
         * - String group identifier of the Pokemon
         */
        return group;
    }

    // function for jackson to set requirement
    public Requirement getrequirement() {
        /*
         * Gets the requirement for the Pokemon.
         * 
         * Returns:
         * - Requirement object for the Pokemon
         */
        return requirement;
    }

    public void setOwned(boolean owned) {
        /*
         * Sets the ownership status of the Pokemon.
         * 
         * Parameters:
         * - owned: boolean indicating new ownership status
         */
        this.owned = owned;
    }

    public void setGroup(String group) {
        /*
         * Sets the group of the Pokemon.
         * 
         * Parameters:
         * - group: String new group identifier
         */
        this.group = group;
    }

    public void setGroupScore(int groupScore) {
        /*
         * Sets the group score of the Pokemon.
         * 
         * Parameters:
         * - groupScore: int new group score
         */
        this.groupScore = groupScore;
    }

    public void setGroupOwner(Player groupOwner) {
        /*
         * Sets the group owner of the Pokemon.
         * 
         * Parameters:
         * - groupOwner: Player who owns the group
         */
        this.groupOwner = groupOwner;
    }

    public Player getGroupOwner() {
        /*
         * Gets the group owner of the Pokemon.
         * 
         * Returns:
         * - Player who owns the group
         */
        return groupOwner;
    }

    public void setOwner(Player owner) {
        /*
         * Sets the owner of this Pokemon and updates related states.
         * 
         * Parameters:
         * - owner: Player who caught the Pokemon
         * 
         * Actions:
         * - Updates ownership status
         * - Adds master ball requirement
         * - Adds Pokemon to owner's collection
         */
        this.owner = owner;
        if (!this.owned) {
            this.owned = true;
            Line masterBall = new Line(0, 0, 0, 1);
            this.requirement.addLine(masterBall);
        }
        owner.addPokemon(this);
    }

    // Function to get requirement lines, used in gamecontroller
    public Requirement getRequirementLines() {
        /*
         * Creates a copy of Pokemon's catching requirements.
         * 
         * Returns:
         * - New Requirement object with copied requirements
         */
        return new Requirement(requirement);
    }
}

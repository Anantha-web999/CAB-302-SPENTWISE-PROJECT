package com.example.trial.settings;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * A custom toggle switch component for JavaFX.
 * This class is a replacement for the standard ToggleButton with a switch-like appearance.
 */
public class ToggleSwitch extends StackPane {
    private final BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    //Color constants
    private static final Color LIGHT_GRAY = Color.rgb(245, 245, 245);
    private static final Color ACCENT_BLUE = Color.rgb(41, 128, 185);

    /**
     * Creates a new toggle switch.
     */
    public ToggleSwitch() {
        //Create background rectangle
        Rectangle background = new Rectangle(50, 25);
        background.setArcWidth(25);
        background.setArcHeight(25);
        background.setFill(LIGHT_GRAY);

        //Create the trigger circle
        Circle trigger = new Circle(12.5);
        trigger.setCenterX(12.5);
        trigger.setCenterY(12.5);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        //Position the trigger on the left side
        StackPane.setAlignment(trigger, Pos.CENTER_LEFT);

        //Add components to the layout
        getChildren().addAll(background, trigger);

        //Set up listener for state changes
        switchedOn.addListener((obs, oldState, newState) -> {
            if (newState) {
                trigger.setTranslateX(25);
                background.setFill(ACCENT_BLUE);
            } else {
                trigger.setTranslateX(0);
                background.setFill(LIGHT_GRAY);
            }
        });

        //Toggle the switch when clicked
        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });

        //Set cursor to hand to indicate clickable
        setCursor(Cursor.HAND);

        //Set minimum size
        setMinSize(50, 25);
        setPrefSize(50, 25);
    }

    /**
     * Gets the switched on property.
     *
     * @return the switched on property
     */
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    /**
     * Checks if the switch is on.
     *
     * @return true if the switch is on, false otherwise
     */
    public boolean isSelected() {
        return switchedOn.get();
    }

    /**
     * Sets the state of the switch.
     *
     * @param selected true to turn the switch on, false to turn it off
     */
    public void setSelected(boolean selected) {
        switchedOn.set(selected);
    }
}
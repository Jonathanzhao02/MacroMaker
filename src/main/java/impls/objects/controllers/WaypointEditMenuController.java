package main.java.impls.objects.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.impls.objects.Waypoint;
import main.java.impls.objects.components.WaypointWidget;
import main.java.impls.objects.events.SelectionEvent;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class WaypointEditMenuController {
    @FXML
    public TextField xTextField;

    @FXML
    public TextField yTextField;

    @FXML
    public TextField durationTextField;

    @FXML
    public TextField delayTextField;

    @FXML
    public Button cancelButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button saveButton;

    @FXML
    public Label errorLabel;

    @FXML
    public CheckBox leftClickCheckBox;

    @FXML
    public CheckBox middleClickCheckBox;

    @FXML
    public CheckBox rightClickCheckBox;

    @FXML
    public CheckBox forwardClickCheckBox;

    @FXML
    public CheckBox backClickCheckBox;

    @FXML
    public CheckBox leftReleaseCheckBox;

    @FXML
    public CheckBox middleReleaseCheckBox;

    @FXML
    public CheckBox rightReleaseCheckBox;

    @FXML
    public CheckBox forwardReleaseCheckBox;

    @FXML
    public CheckBox backReleaseCheckBox;

    private Waypoint waypoint;
    private WaypointWidget parent;

    public void initialize() {

    }

    public void initData(Waypoint waypoint, WaypointWidget parent) {
        this.waypoint = waypoint;
        this.parent = parent;

        xTextField.setText(Double.toString(waypoint.getX()));
        yTextField.setText(Double.toString(waypoint.getY()));

        durationTextField.setText(Integer.toString(waypoint.getDuration()));
        delayTextField.setText(Integer.toString(waypoint.getDelay()));

        cancelButton.setOnAction(e -> {
            parent.closeEditMenu();
        });

        deleteButton.setOnAction(e -> {
            parent.fireEvent(new SelectionEvent(this.waypoint, deleteButton, parent.getParent()));
            parent.closeEditMenu();
        });

        saveButton.setOnAction(e -> {
            validateFields();
        });
    }

    private void validateFields() {
        // TODO:
        // MAKE VALIDATION LOOK BETTER
        double x = -1;
        double y = -1;
        int duration = -1;
        int delay = -1;

        setErrorText("");

        try {
            x = Double.parseDouble(xTextField.getText());

            if (x < 0) {
                appendErrorText("\nX MUST BE GREATER THAN OR EQUAL TO 0");
            }

        } catch (NumberFormatException e) {
            appendErrorText("\nX MUST BE A NUMBER");
        }

        try {
            y = Double.parseDouble(yTextField.getText());

            if (y < 0) {
                appendErrorText("\nY MUST BE GREATER THAN OR EQUAL TO 0");
            }

        } catch (NumberFormatException e) {
            appendErrorText("\nY MUST BE A NUMBER");
        }

        try {
            duration = Integer.parseInt(durationTextField.getText());

            if (duration <= 0) {
                appendErrorText("\nDURATION MUST BE GREATER THAN 0");
            }

        } catch (NumberFormatException e) {
            appendErrorText("\nDURATION MUST BE AN INTEGER");
        }

        try {
            delay = Integer.parseInt(delayTextField.getText());

            if (delay < 0) {
                appendErrorText("\nDELAY MUST BE GREATER THAN OR EQUAL TO 0");
            }

        } catch (NumberFormatException e) {
            appendErrorText("\nDELAY MUST BE AN INTEGER");
        }

        if (x >= 0 && y >= 0 && duration > 0 && delay >= 0) {
            waypoint.setDelay(delay);
            waypoint.setLocation(new Point((int) x, (int) y));
            waypoint.setDuration(duration);
            parent.closeEditMenu();
        }

    }

    private void appendErrorText(String text) {
        setErrorText(errorLabel.getText() + text);
    }

    private void setErrorText(String text) {
        errorLabel.setText(text);
    }
}

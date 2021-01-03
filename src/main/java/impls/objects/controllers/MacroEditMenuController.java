package main.java.impls.objects.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.java.impls.enums.MacroMode;
import main.java.impls.objects.Activator;
import main.java.impls.objects.MacroDisplay;
import main.java.impls.objects.hotkeys.MacroHotkey;

public class MacroEditMenuController {
    @FXML
    public VBox menuBox;

    @FXML
    public Label nameLabel;

    @FXML
    public RadioButton mouseOnlyRadio;

    @FXML
    public RadioButton clicksOnlyRadio;

    @FXML
    public RadioButton keysOnlyRadio;

    @FXML
    public RadioButton allRadio;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    private MacroHotkey macro;

    public void initialize() {

    }

    public void initData(MacroHotkey macro, Activator inhibitor) {
        this.macro = macro;
        nameLabel.setText(macro.getName());

        switch (macro.getMode()) {
            case ALL -> allRadio.setSelected(true);
            case KEYS_ONLY -> keysOnlyRadio.setSelected(true);
            case CLICKS_ONLY -> clicksOnlyRadio.setSelected(true);
            case MOUSE_ONLY -> mouseOnlyRadio.setSelected(true);
        }

        mouseOnlyRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.MOUSE_ONLY);
            e.consume();
        });

        clicksOnlyRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.CLICKS_ONLY);
            e.consume();
        });

        keysOnlyRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.KEYS_ONLY);
            e.consume();
        });

        allRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.ALL);
            e.consume();
        });

        editButton.setOnAction(e -> {
            MacroDisplay display = new MacroDisplay(inhibitor);
            display.displayMacro(this.macro);
            e.consume();
        });

        deleteButton.setOnAction(e -> {
            menuBox.fireEvent(new ActionEvent(menuBox, menuBox.getParent()));
            e.consume();
        });
    }
}

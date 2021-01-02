package main.java.impls.objects.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.java.impls.enums.MacroMode;
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
    public RadioButton keysOnlyRadio;

    @FXML
    public RadioButton bothRadio;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    private MacroHotkey macro;

    public void initialize() {

    }

    public void initData(MacroHotkey macro) {
        this.macro = macro;
        nameLabel.setText(macro.getName());

        switch (macro.getMode()) {
            case BOTH -> bothRadio.setSelected(true);
            case KEYS_ONLY -> keysOnlyRadio.setSelected(true);
            case MOUSE_ONLY -> mouseOnlyRadio.setSelected(true);
        }

        mouseOnlyRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.MOUSE_ONLY);
            e.consume();
        });

        keysOnlyRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.KEYS_ONLY);
            e.consume();
        });

        bothRadio.setOnAction(e -> {
            this.macro.setMode(MacroMode.BOTH);
            e.consume();
        });

        editButton.setOnAction(e -> {
            MacroDisplay display = new MacroDisplay();
            display.displayMacro(this.macro);
            e.consume();
        });

        deleteButton.setOnAction(e -> {
            menuBox.fireEvent(new ActionEvent(menuBox, menuBox.getParent()));
            e.consume();
        });
    }
}

package main.java.impls.objects.controllers;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.java.UserPreferences;
import main.java.impls.enums.*;
import main.java.impls.objects.components.MacroTableWrapper;
import main.java.impls.objects.events.SelectionEvent;
import main.java.impls.objects.hotkeys.*;
import main.java.impls.objects.*;
import main.java.impls.objects.nativelisteners.HotkeyKeyListener;
import main.java.impls.objects.nativelisteners.SingleKeyListener;
import main.java.impls.utils.*;
import main.java.interfaces.ActivationListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    public VBox mainBox;

    @FXML
    public Pane detailsBox;

    @FXML
    public Pane macroEditorBox;

    @FXML
    public ScrollPane macrosBox;

    @FXML
    public GridPane settingsBox;

    @FXML
    public Label leftStatusLabel;

    @FXML
    public Label rightStatusLabel;

    @FXML
    public TableView<MacroHotkey> macroTable;

    private static final UserPreferences preferences = new UserPreferences();

    private Activator recorder;
    private Activator inhibitor;
    private MacroRecorder macroRecorder;
    private HotkeyKeyListener hotkeyKeyListener;
    private EscapeHotkey escapeHotkey;
    private RecordHotkey recordHotkey;

    private ObservableList<MacroHotkey> macros;
    private MacroTableWrapper macroView;

    public void initialize() {
        // App logic
        recorder = new Activator();
        inhibitor = new Activator();
        macroRecorder = new MacroRecorder();
        hotkeyKeyListener = new HotkeyKeyListener();
        escapeHotkey = new EscapeHotkey(preferences.getKeySetting(KeySetting.QUIT_KEY));
        recordHotkey = new RecordHotkey(preferences.getKeySetting(KeySetting.RECORD_KEY), recorder, inhibitor);

        recorder.addListener(new ActivationListener() {
            @Override
            public void onActivate() {
                macroRecorder.activate(preferences.getOptionSetting(OptionSetting.SHOW_FRAME_ON_RECORD));
                hotkeyKeyListener.addInhibitorException(recordHotkey);
                Platform.runLater(() -> rightStatusLabel.setText("Recording!"));
            }

            @Override
            public void onDeactivate() {
                List<Waypoint> waypoints = macroRecorder.deactivate();
                waypoints = MacroUtils.preprocess(waypoints, preferences.getDoubleSetting(DoubleSetting.EPSILON));

                if (macros != null && !waypoints.isEmpty()) {
                    MacroHotkey macroHotkey = new MacroHotkey(NativeKeyEvent.VC_UNDEFINED, waypoints);
                    String defaultName = "default";
                    var ref = new Object() {
                        int id = 0;
                    };

                    while (macros.stream().anyMatch(macroHotkey1 -> macroHotkey1.getName().equals(defaultName + ref.id))) {
                        ref.id++;
                    }

                    macroHotkey.setName(defaultName + ref.id);
                    macros.add(macroHotkey);
                    hotkeyKeyListener.setHotkey(macroHotkey);
                    hotkeyKeyListener.removeInhibitorException(recordHotkey);
                }

                Platform.runLater(() -> rightStatusLabel.setText(""));
            }
        });

        hotkeyKeyListener.setHotkey(escapeHotkey);
        hotkeyKeyListener.setHotkey(recordHotkey);
        hotkeyKeyListener.setInhibitor(inhibitor);
        hotkeyKeyListener.addInhibitorException(escapeHotkey);

        MacroHotkey.setRecorder(recorder);
        MacroHotkey.setInhibitor(inhibitor);
        MacroHotkey.setLinear(preferences.getOptionSetting(OptionSetting.LINEAR_INTERPOLATION));
        Waypoint.setDelayThreshold((int) preferences.getDoubleSetting(DoubleSetting.DELAY_THRESHOLD));
        MacroDisplay.setDisplayInterval(preferences.getDoubleSetting(DoubleSetting.DISPLAY_INTERVAL));

        preferences.addListener(e -> {
            String key = e.getKey();

            if (key.equals(OptionSetting.LINEAR_INTERPOLATION.toString())) {
                MacroHotkey.setLinear(Boolean.parseBoolean(e.getNewValue()));
            } else if (key.equals(DoubleSetting.DELAY_THRESHOLD.toString())) {
                Waypoint.setDelayThreshold((int) Double.parseDouble(e.getNewValue()));
            } else if (key.equals(DoubleSetting.DISPLAY_INTERVAL.toString())) {
                MacroDisplay.setDisplayInterval(Double.parseDouble(e.getNewValue()));
            } else if (key.equals(KeySetting.QUIT_KEY.toString())) {
                escapeHotkey.setKey(Integer.parseInt(e.getNewValue()));
            } else if (key.equals(KeySetting.RECORD_KEY.toString())) {
                recordHotkey.setKey(Integer.parseInt(e.getNewValue()));
            }
        });

        NativeEnumMapper.initialize();

        // UI Logic
        macroView = new MacroTableWrapper(macroTable);
        macroView.initialize(inhibitor, leftStatusLabel);
        macros = FXCollections.observableArrayList();
        macroTable.setItems(macros);

        // TODO:
        // IMPROVE WAYPOINT DISPLAY
        // on loading macros from file, initialize settings, transient properties
        // MACRO PIPING?
        // BETTER SETTINGS SETTING (more intuitive, new scene entirely?)

        macros.addListener((ListChangeListener<? super MacroHotkey>) c -> {
            Platform.runLater(() -> macroEditorBox.getChildren().clear());
        });

        macroTable.addEventHandler(SelectionEvent.SELECTION_EVENT, e -> {
            if (e.getObject().getClass() == MacroHotkey.class) {
                MacroHotkey current = (MacroHotkey) e.getObject();
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main/resources/macro_editor.fxml")
                );

                try {
                    VBox editMenu = loader.load();
                    macroEditorBox.getChildren().setAll(editMenu);
                    editMenu.prefWidthProperty().bind(macroEditorBox.widthProperty());
                    editMenu.prefHeightProperty().bind(macroEditorBox.heightProperty());

                    MacroEditMenuController controller = loader.getController();
                    controller.initData(current);

                    editMenu.addEventHandler(ActionEvent.ANY, event -> {
                        current.setKey(-1);
                        macros.remove(current);
                    });
                } catch (IOException ignored) {

                }

            }
        });

        settingsBox.setVgap(10);
        settingsBox.setHgap(10);
        settingsBox.setAlignment(Pos.CENTER);

        int sum = 0;

        for (KeySetting setting : KeySetting.values()) {
            Tooltip tip = new Tooltip();
            Label field = new Label();
            Label lbl = new Label();
            String name = switch (setting) {
                case QUIT_KEY -> "Quit Key";
                case RECORD_KEY -> "Record Key";
            };

            tip.setText(setting.getDesc());
            lbl.setTooltip(tip);
            field.setTooltip(tip);

            lbl.setText(name);
            field.setText(NativeKeyEvent.getKeyText(preferences.getKeySetting(setting)));
            field.setOnMouseClicked(e -> {
                leftStatusLabel.setText("Now editing hotkey for " + name);
                inhibitor.activate();
                GlobalScreen.addNativeKeyListener(new SingleKeyListener(
                    key -> {

                        try {
                            Thread.sleep(500);
                        } catch (Exception ignored) {

                        }

                        Platform.runLater(() -> {
                            inhibitor.deactivate();
                            preferences.setKeySetting(setting, key);
                            leftStatusLabel.setText("");
                            field.setText(NativeKeyEvent.getKeyText(key));
                        });
                        return null;
                    }
                ));
            });
            settingsBox.add(lbl, 0, sum);
            settingsBox.add(field, 1, sum);
            sum++;
        }

        for (DoubleSetting setting : DoubleSetting.values()) {
            Tooltip tip = new Tooltip();
            TextField field = new TextField();
            field.setPrefWidth(40);
            Label lbl = new Label();
            String name = switch (setting) {
                case EPSILON -> "Epsilon";
                case DELAY_THRESHOLD -> "Delay Threshold";
                case DISPLAY_INTERVAL -> "Display Interval";
            };

            field.setText(Double.toString(preferences.getDoubleSetting(setting)));
            field.setOnAction(e -> {
                try {
                    double val = Double.parseDouble(field.getText());

                    switch (setting) {
                        case EPSILON:
                            if (val < 0) val = 0;
                            break;
                        case DISPLAY_INTERVAL:
                        case DELAY_THRESHOLD:
                            if (val < 1) val = 1;
                            break;
                    }

                    preferences.setDoubleSetting(setting, val);
                } catch (Exception exc) {
                    field.setText(Double.toString(preferences.getDoubleSetting(setting)));
                }
            });

            tip.setText(setting.getDesc());
            lbl.setTooltip(tip);
            field.setTooltip(tip);

            lbl.setText(name);
            settingsBox.add(lbl, 0, sum);
            settingsBox.add(field, 1, sum);
            sum++;
        }

        for (OptionSetting setting : OptionSetting.values()) {
            Tooltip tip = new Tooltip();
            CheckBox field = new CheckBox();
            Label lbl = new Label();
            String name = switch (setting) {
                case LINEAR_INTERPOLATION -> "Linear Interpolation";
                case SHOW_FRAME_ON_RECORD -> "Show Recording Frame";
            };

            field.setSelected(preferences.getOptionSetting(setting));
            field.setOnAction(e -> {
                preferences.setOptionSetting(setting, field.isSelected());
            });

            tip.setText(setting.getDesc());
            lbl.setTooltip(tip);
            field.setTooltip(tip);

            lbl.setText(name);
            settingsBox.add(lbl, 0, sum);
            settingsBox.add(field, 1, sum);
            sum++;
        }

        // Finishing up
        GlobalScreen.addNativeKeyListener(hotkeyKeyListener);
        System.out.println("Initialized!");
    }
}

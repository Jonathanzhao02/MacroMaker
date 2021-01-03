package main.java.impls.objects.components;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseButton;
import main.java.impls.objects.nativelisteners.SingleKeyListener;
import main.java.impls.objects.hotkeys.MacroHotkey;
import main.java.impls.objects.events.ActivationEmitter;
import main.java.interfaces.ActivationListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;

public class MacroKeyTableCell extends TableCell<MacroHotkey, String> {
    private ActivationEmitter emitter = new ActivationEmitter();
    private boolean awaiting;
    private Label statusLabel;

    public MacroKeyTableCell(Label statusLabel) {
        super();
        this.statusLabel = statusLabel;
        setOnMouseClicked(e -> {
            if (isEmpty()) return;

            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && !awaiting) {
                awaiting = true;
                activate();
            }
        });
    }

    public void addListener(ActivationListener listener) {
        emitter.addListener(listener);
    }

    public void removeListener(ActivationListener listener) {
        emitter.removeListener(listener);
    }

    public void activate() {
        GlobalScreen.addNativeKeyListener(new SingleKeyListener(
            (nativeKey, rawKey) -> Platform.runLater(() -> {
                getTableRow().getItem().setKey(nativeKey);
                getTableView().refresh();
                deactivate();
            })
        ));
        Platform.runLater(() -> {
            statusLabel.setText(String.format("Editing hotkey for %s", getTableRow().getItem().getName()));
        });
        emitter.activate();
    }

    public void deactivate() {
        Platform.runLater(() -> {
            statusLabel.setText("");
        });
        emitter.deactivate();
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            setText(item.equals(NativeKeyEvent.getKeyText(NativeKeyEvent.VC_UNDEFINED)) ? "" : item);
        } else {
            setText(null);
        }
    }
}

package main.java.impls.objects.hotkeys;

import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class EscapeHotkey extends Hotkey {
    public EscapeHotkey(int key) {
        super(key);
    }

    @Override
    public void run() {
        try {
            System.out.println("QUITTING");
            GlobalScreen.unregisterNativeHook();
            Platform.runLater(() -> {
                System.exit(0);
            });
            Platform.exit();
        } catch (NativeHookException nativeHookException) {
            nativeHookException.printStackTrace();
            System.exit(1);
        }
    }
}

package main.java.impls.objects.nativelisteners;

import javafx.util.Callback;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class SingleKeyListener implements NativeKeyListener {
    private final Callback<Integer, Void> callback;

    public SingleKeyListener(Callback<Integer, Void> callback) {
        this.callback = callback;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        callback.call(nativeKeyEvent.getKeyCode());
        GlobalScreen.removeNativeKeyListener(this);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}

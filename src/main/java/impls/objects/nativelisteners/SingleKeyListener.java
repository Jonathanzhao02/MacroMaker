package main.java.impls.objects.nativelisteners;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.function.BiConsumer;

public class SingleKeyListener implements NativeKeyListener {
    private final BiConsumer<Integer, Integer> consumer;

    public SingleKeyListener(BiConsumer<Integer, Integer> callback) {
        this.consumer = callback;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        consumer.accept(nativeKeyEvent.getKeyCode(), nativeKeyEvent.getRawCode());
        GlobalScreen.removeNativeKeyListener(this);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}

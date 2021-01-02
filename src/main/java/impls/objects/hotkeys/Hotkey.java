package main.java.impls.objects.hotkeys;

import main.java.impls.objects.events.KeyChangeEmitter;
import main.java.interfaces.KeyChangeListener;

public class Hotkey implements Runnable {
    protected static final KeyChangeEmitter emitter = new KeyChangeEmitter();
    private int key;

    public Hotkey(int key) {
        this.key = key;
    }

    public boolean condition() {
        return true;
    }

    public void prerun() {
        // Do nothing
    }

    @Override
    public void run() {
        // Do nothing
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        emitter.changeKey(this.key, key, this);
        this.key = key;
    }

    public static void addKeyChangeListener(KeyChangeListener listener) {
        emitter.addListener(listener);
    }

    public static void removeKeyChangeListener(KeyChangeListener listener) {
        emitter.removeListener(listener);
    }
}

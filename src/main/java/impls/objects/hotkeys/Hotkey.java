package main.java.impls.objects.hotkeys;

import main.java.impls.objects.events.KeyChangeEmitter;
import main.java.interfaces.KeyChangeListener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Hotkey implements Runnable, Serializable {
    @Serial
    private static final long serialVersionUID = Long.parseLong("681923370");
    protected static final KeyChangeEmitter emitter = new KeyChangeEmitter();

    protected final UUID id = UUID.randomUUID();
    private int key = -1;

    public Hotkey() {

    }

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
        if (key == -1) {
            tryDelete();
        }
    }

    private void tryDelete() {
        String fileName = System.getProperty("user.home") +
                File.separator + "AppData" +
                File.separator + "local" +
                File.separator + "MacroMaker" +
                File.separator + id.toString() + ".macro";
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addKeyChangeListener(KeyChangeListener listener) {
        emitter.addListener(listener);
    }

    public static void removeKeyChangeListener(KeyChangeListener listener) {
        emitter.removeListener(listener);
    }
}

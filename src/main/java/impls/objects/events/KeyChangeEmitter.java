package main.java.impls.objects.events;

import main.java.impls.objects.hotkeys.Hotkey;
import main.java.interfaces.Emitter;
import main.java.interfaces.KeyChangeListener;

public class KeyChangeEmitter extends Emitter<KeyChangeListener> {
    public void changeKey(int oldKey, int newKey, Hotkey hotkey) {
        for (KeyChangeListener listener : listeners) {
            listener.onKeyChange(oldKey, newKey, hotkey);
        }
    }
}

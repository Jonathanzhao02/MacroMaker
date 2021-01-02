package main.java.interfaces;

import main.java.impls.objects.hotkeys.Hotkey;

import java.util.EventListener;

public interface KeyChangeListener extends EventListener {
    void onKeyChange(int oldKey, int newKey, Hotkey hotkey);
}

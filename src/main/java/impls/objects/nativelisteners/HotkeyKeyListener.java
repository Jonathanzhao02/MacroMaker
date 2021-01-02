package main.java.impls.objects.nativelisteners;

import main.java.impls.objects.Activator;
import main.java.impls.objects.hotkeys.Hotkey;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.*;
import java.util.stream.Collectors;

public class HotkeyKeyListener implements NativeKeyListener {
    private Set<Hotkey> hotkeys = new HashSet<>();
    private Set<Hotkey> inhibitorExceptions = new HashSet<>();
    private Map<Integer, Boolean> keysPressed = new HashMap<>();
    private Activator inhibitor;

    public HotkeyKeyListener() {
        super();
        Hotkey.addKeyChangeListener((oldKey, newKey, hotkey) -> {
            if (newKey == -1) {
                removeHotkey(hotkey);
            }
        });
    }

    public void setInhibitor(Activator inhibitor) {
        this.inhibitor = inhibitor;
    }

    public void addInhibitorException(Hotkey hotkey) {
        inhibitorExceptions.add(hotkey);
    }

    public void removeInhibitorException(Hotkey hotkey) {
        inhibitorExceptions.remove(hotkey);
    }

    public void setHotkey(Hotkey newHotkey) {
        hotkeys.add(newHotkey);
    }

    public void removeHotkey(Hotkey hotkey) {
        hotkeys.remove(hotkey);
        removeInhibitorException(hotkey);
    }

    public Set<Hotkey> getHotkeysFor(int key) {
        return hotkeys.stream().filter(hotkey -> hotkey.getKey() == key).collect(Collectors.toCollection(HashSet::new));
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (keysPressed.getOrDefault(e.getKeyCode(), false)) return;

        for (Hotkey hotkey : getHotkeysFor(e.getKeyCode())) {
            if ((!inhibitor.isOn() || inhibitorExceptions.contains(hotkey)) && hotkey.condition()) {
                hotkey.prerun();
                new Thread(hotkey).start();
            }
        }

        keysPressed.put(e.getKeyCode(), true);
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        keysPressed.put(e.getKeyCode(), false);
        // System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        // System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }
}
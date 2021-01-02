package main.java.impls.objects.hotkeys;

import main.java.impls.objects.Activator;

public class RecordHotkey extends Hotkey {
    private final Activator activator;
    private final Activator inhibitor;

    public RecordHotkey(int key, Activator activator, Activator inhibitor) {
        super(key);
        this.activator = activator;
        this.inhibitor = inhibitor;
    }

    @Override
    public void run() {
        if (!activator.isOn()) {
            activator.activate();
            inhibitor.activate();
        } else {
            activator.deactivate();
            inhibitor.deactivate();
        }
    }
}

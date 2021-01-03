package main.java.impls.objects;

import main.java.impls.enums.MacroMode;

import java.awt.*;
import java.awt.event.*;

public class MacroRobot extends Robot {
    private Activator inhibitor;

    public MacroRobot(Activator inhibitor) throws AWTException {
        super();
        this.inhibitor = inhibitor;
    }

    public void activate(Waypoint waypoint, MacroMode mode) {
        if (waypoint.hasLocation() && mode != MacroMode.KEYS_ONLY) {
            mouseMove((int) waypoint.getX(), (int) waypoint.getY());
        }

        if (waypoint.hasKeys() && mode != MacroMode.MOUSE_ONLY) {
            for (int key : waypoint.getKeys()) {
                keyPress(key);
            }
        }

        if (waypoint.hasClickButtons() && mode != MacroMode.KEYS_ONLY) {
            int mask = 0x0;
            for (int mouseButton : waypoint.getClickButtons()) {
                if (mouseButton == MouseEvent.NOBUTTON) continue;
                mask |= InputEvent.getMaskForButton(mouseButton);
            }
            mousePress(mask);
        }

        if (waypoint.hasReleaseKeys() && mode != MacroMode.MOUSE_ONLY) {
            for (int key : waypoint.getReleaseKeys()) {
                keyRelease(key);
            }
        }

        if (waypoint.hasReleaseButtons() && mode != MacroMode.KEYS_ONLY) {
            int mask = 0x0;
            for (int mouseButton : waypoint.getReleaseButtons()) {
                if (mouseButton == MouseEvent.NOBUTTON) continue;
                mask |= InputEvent.getMaskForButton(mouseButton);
            }
            mouseRelease(mask);
        }
    }

    @Override
    public synchronized void keyPress(int keycode) {
        inhibitor.activate();
        super.keyPress(keycode);
        inhibitor.deactivate();
    }

    public void mouseMove(double x, double y) {
        mouseMove((int) x, (int) y);
    }
}

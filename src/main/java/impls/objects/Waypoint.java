package main.java.impls.objects;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Waypoint {
    private static int delayThreshold;

    private Point location;
    private List<Integer> keys;
    private List<Integer> nativeKeys;
    private List<Integer> clickButtons;
    private List<Integer> releaseKeys;
    private List<Integer> nativeReleaseKeys;
    private List<Integer> releaseButtons;
    private int duration;
    private int delay;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean hasLocation() {
        return location != null;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }

    public List<Integer> getKeys() {
        return keys;
    }

    public List<Integer> getNativeKeys() {
        return nativeKeys;
    }

    public void addKey(int key, int nativeKey) {
        if (!hasKeys()) {
            keys = new LinkedList<>();
            nativeKeys = new LinkedList<>();
        }
        nativeKeys.add(nativeKey);
        keys.add(key);
    }

    public void setKeys(List<Integer> keys, List<Integer> nativeKeys) {
        this.keys = keys;
        this.nativeKeys = nativeKeys;
    }

    public void clearKeys() {
        this.keys = null;
        this.nativeKeys = null;
    }

    public List<Integer> getReleaseKeys() {
        return releaseKeys;
    }

    public List<Integer> getNativeReleaseKeys() {
        return nativeReleaseKeys;
    }

    public void addReleaseKey(int key, int nativeKey) {
        if (!hasReleaseKeys()) {
            releaseKeys = new LinkedList<>();
            nativeReleaseKeys = new LinkedList<>();
        }
        nativeReleaseKeys.add(nativeKey);
        releaseKeys.add(key);
    }

    public void setReleaseKeys(List<Integer> keys, List<Integer> nativeKeys) {
        this.releaseKeys = keys;
        this.nativeReleaseKeys = nativeKeys;
    }

    public void clearReleaseKeys() {
        this.releaseKeys = null;
        this.nativeReleaseKeys = null;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int delay) {
        this.duration = delay;
    }

    public boolean isDelayed() {
        return delay > 0;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isMouseOnly() {
        return !(hasKeys() || hasReleaseKeys() || hasClickButtons() || hasReleaseButtons() || isDelayed());
    }

    public boolean hasKeys() {
        return keys != null && !keys.isEmpty();
    }

    public boolean hasClickButton(int button) {
        if (!hasClickButtons()) return false;
        for (int mouseButton : clickButtons) {
            if (mouseButton == button) return true;
        }

        return false;
    }

    public boolean hasClickButtons() {
        return clickButtons != null && !clickButtons.isEmpty();
    }

    public boolean hasReleaseKeys() {
        return releaseKeys != null && !releaseKeys.isEmpty();
    }

    public boolean hasReleaseButton(int button) {
        if (!hasReleaseButtons()) return false;
        for (int mouseButton : releaseButtons) {
            if (mouseButton == button) return true;
        }

        return false;
    }

    public boolean hasReleaseButtons() {
        return releaseButtons != null && !releaseButtons.isEmpty();
    }

    public void setClickButtons(List<Integer> mouseButtons) {
        this.clickButtons = mouseButtons;
    }
    
    public List<Integer> getClickButtons() {
        return clickButtons;
    }

    public void setReleaseButtons(List<Integer> mouseButtons) {
        this.releaseButtons = mouseButtons;
    }
    
    public List<Integer> getReleaseButtons() {
        return releaseButtons;
    }

    public static int getDelayThreshold() {
        return delayThreshold;
    }

    public static void setDelayThreshold(int threshold) {
        Waypoint.delayThreshold = threshold;
    }
}

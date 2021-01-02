package main.java.impls.objects;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Waypoint {
    private static int delayThreshold;

    private Point location;
    private List<Integer> keys;
    private List<Integer> nativeKeys;
    private int[] mouseButtons;
    private List<Integer> releaseKeys;
    private List<Integer> nativeReleaseKeys;
    private int[] releaseMouseButtons;
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
        return !(hasKeys() || hasReleaseKeys() || hasMouseButtons() || hasReleaseMouseButtons() || isDelayed());
    }

    public boolean hasKeys() {
        return keys != null && !keys.isEmpty();
    }

    public boolean hasMouseButton(int button) {
        for (int mouseButton : mouseButtons) {
            if (mouseButton == button) return true;
        }

        return false;
    }

    public boolean hasMouseButtons() {
        return mouseButtons != null && mouseButtons.length > 0;
    }

    public boolean hasReleaseKeys() {
        return releaseKeys != null && !releaseKeys.isEmpty();
    }

    public boolean hasReleaseMouseButton(int button) {
        for (int mouseButton : releaseMouseButtons) {
            if (mouseButton == button) return true;
        }

        return false;
    }

    public boolean hasReleaseMouseButtons() {
        return releaseMouseButtons != null && releaseMouseButtons.length > 0;
    }

    public void setMouseButtons(int[] mouseButtons) {
        this.mouseButtons = mouseButtons;
    }

    public void clearMouseButtons() {
        this.mouseButtons = null;
    }

    public int[] getMouseButtons() {
        return mouseButtons;
    }

    public void setReleaseMouseButtons(int[] mouseButtons) {
        this.releaseMouseButtons = mouseButtons;
    }

    public void clearReleaseMouseButtons() {
        this.releaseMouseButtons = null;
    }

    public int[] getReleaseMouseButtons() {
        return releaseMouseButtons;
    }

    public static int getDelayThreshold() {
        return delayThreshold;
    }

    public static void setDelayThreshold(int threshold) {
        Waypoint.delayThreshold = threshold;
    }
}

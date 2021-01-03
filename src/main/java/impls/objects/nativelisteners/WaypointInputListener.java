package main.java.impls.objects.nativelisteners;

import main.java.impls.objects.Waypoint;
import main.java.impls.utils.NativeEnumMapper;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WaypointInputListener implements NativeMouseInputListener, NativeKeyListener {
    private List<Waypoint> waypoints;
    private long bufferTime;
    private Map<Integer, Boolean> keysPressed = new HashMap<>();

    public WaypointInputListener() {
        reset();
    }

    public void reset() {
        this.waypoints = new LinkedList<>();
        this.bufferTime = System.currentTimeMillis() + 100; // don't record for first 100 ms
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    private Waypoint createWaypoint(long timestamp) {
        Waypoint waypoint = new Waypoint();
        waypoint.setTimestamp(timestamp);
        waypoints.add(waypoint);
        return waypoint;
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // System.out.println("Mouse Clicked: " + e.getClickCount());
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime) return;
        Waypoint waypoint = createWaypoint(timestamp);
        waypoint.setLocation(e.getPoint());
        List<Integer> buttons = new LinkedList<>();
        buttons.add(NativeEnumMapper.nativeToJavaMouse(e.getButton()));
        waypoint.setClickButtons(buttons);
        // System.out.println("Mouse Pressed: " + e.getButton());
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime) return;
        Waypoint waypoint = createWaypoint(timestamp);
        waypoint.setLocation(e.getPoint());
        List<Integer> buttons = new LinkedList<>();
        buttons.add(NativeEnumMapper.nativeToJavaMouse(e.getButton()));
        waypoint.setReleaseButtons(buttons);
        // System.out.println("Mouse Released: " + e.getButton());
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime) return;
        Waypoint waypoint = createWaypoint(timestamp);
        waypoint.setLocation(e.getPoint());
        // System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime) return;
        Waypoint waypoint = createWaypoint(timestamp);
        waypoint.setLocation(e.getPoint());
        // System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime || keysPressed.getOrDefault(e.getKeyCode(), false)) return;
        Waypoint waypoint = createWaypoint(timestamp);
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        waypoint.setLocation(mousePos);
        List<Integer> keys = new LinkedList<>();
        List<Integer> nativeKeys = new LinkedList<>();
        keys.add(e.getRawCode());
        nativeKeys.add(e.getKeyCode());
        waypoint.setKeys(keys, nativeKeys);
        keysPressed.put(e.getKeyCode(), true);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < bufferTime || !keysPressed.getOrDefault(e.getKeyCode(), false)) return;
        Waypoint waypoint = createWaypoint(timestamp);
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        waypoint.setLocation(mousePos);
        List<Integer> keys = new LinkedList<>();
        List<Integer> nativeKeys = new LinkedList<>();
        keys.add(e.getRawCode());
        nativeKeys.add(e.getKeyCode());
        waypoint.setReleaseKeys(keys, nativeKeys);
        keysPressed.put(e.getKeyCode(), false);
    }
}
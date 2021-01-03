package main.java.impls.objects.hotkeys;

import main.java.impls.enums.MacroMode;
import main.java.impls.objects.*;
import main.java.impls.utils.MacroUtils;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MacroHotkey extends Hotkey implements Serializable {
    @Serial
    private static final long serialVersionUID = Long.parseLong("987253894");

    private static boolean linear;
    private static Activator recorder;
    private static Activator inhibitor;

    private transient TimeSeriesInterpolator interpolator;
    private transient boolean running;

    private MacroMode mode = MacroMode.BOTH;
    private List<Waypoint> waypoints;
    private String name;
    private int loops;

    public static void setRecorder(Activator recorder) {
        MacroHotkey.recorder = recorder;
    }

    public static void setInhibitor(Activator inhibitor) {
        MacroHotkey.inhibitor = inhibitor;
    }

    public static boolean isLinear() {
        return linear;
    }

    public static void setLinear(boolean linear) {
        MacroHotkey.linear = linear;
    }

    public MacroHotkey(int key, List<Waypoint> waypoints) {
        super(key);
        this.loops = 1;
        setWaypoints(waypoints);
    }

    public void initialize() {
        MacroUtils.assignTimestamps(waypoints);
        createInterpolators();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize();
    }

    @Override
    public boolean condition() {
        return (waypoints != null && !waypoints.isEmpty() && !recorder.isOn()) || running;
    }

    @Override
    public void prerun() {
        running = !running;
    }

    @Override
    public void run() {
        if (running) {
            try {
                MacroRobot robot = new MacroRobot(inhibitor);
                int numLoops = 0;

                while (running && (loops == 0 || numLoops < loops)) {
                    runMacro(robot);
                    numLoops++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                running = false;
            }
        }
    }

    private void runMacro(MacroRobot robot) {
        int interval = 5;
        long maxTime = waypoints.get(waypoints.size() - 1).getTimestamp();
        long delayedUntil = 0;
        int idx = 0;
        Waypoint next = waypoints.get(idx);

        double[] coords = {0, 0};

        for (long i = 0; i <= maxTime; i += interval) {
            if (!running) break;

            if (mode != MacroMode.KEYS_ONLY) {
                if (i >= delayedUntil) coords = interpolator.interpolate(i, linear);
                robot.mouseMove(coords[0], coords[1]);
            }

            while (Math.abs(next.getTimestamp() - i) < interval) {
                if (!next.isMouseOnly()) {
                    robot.activate(next, mode);
                }

                if (next.isDelayed()) {
                    delayedUntil = next.getDelay() + next.getTimestamp();
                }

                idx++;

                if (idx < waypoints.size()) {
                    next = waypoints.get(idx);
                } else {
                    break;
                }
            }

            robot.delay(interval);
        }
    }

    public void interrupt() {
        running = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLoops(int num) {
        loops = num;
    }

    public int getLoops() {
        return loops;
    }

    public void setMode(MacroMode mode) {
        this.mode = mode;
    }

    public MacroMode getMode() {
        return mode;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
        initialize();
    }

    public void createInterpolators() {
        List<Waypoint> processedPoints = new LinkedList<>(waypoints);
        MacroUtils.sortedAddAll(processedPoints, MacroUtils.dupeDelays(
            waypoints.stream().filter(Waypoint::isDelayed).collect(Collectors.toList())
        ));

        double[] samplePointsX = processedPoints
                .stream()
                .map(Waypoint::getLocation)
                .mapToDouble(Point::getX)
                .toArray();

        double[] samplePointsY = processedPoints
                .stream()
                .map(Waypoint::getLocation)
                .mapToDouble(Point::getY)
                .toArray();

        double[] timeseries = processedPoints
                .stream()
                .mapToDouble(Waypoint::getTimestamp)
                .toArray();

        interpolator = new TimeSeriesInterpolator(timeseries, samplePointsX, samplePointsY);
    }

    public List<Double> getInterpolatedX(double interval) {
        long maxTime = waypoints.stream().mapToLong(Waypoint::getTimestamp).max().orElse(0);
        long minTime = waypoints.stream().mapToLong(Waypoint::getTimestamp).min().orElse(0);
        return interpolator.getInterpolatedX(minTime, maxTime, interval, linear);
    }

    public List<Double> getInterpolatedY(double interval) {
        long maxTime = waypoints.stream().mapToLong(Waypoint::getTimestamp).max().orElse(0);
        long minTime = waypoints.stream().mapToLong(Waypoint::getTimestamp).min().orElse(0);
        return interpolator.getInterpolatedY(minTime, maxTime, interval, linear);
    }

    public void deleteWaypoint(Waypoint waypoint) {
        waypoints.remove(waypoint);
    }

    public void addEmptyWaypoint(int idx) {
        int before = idx - 1;
        int after = idx;
        if (before < 0 || after >= waypoints.size()) return;

        Waypoint current = new Waypoint(), prev, next;

        prev = waypoints.get(before);
        next = waypoints.get(after);

        Point avgLoc = new Point((int) (0.5 * (prev.getX() + next.getX())), (int) (0.5 * (prev.getY() + next.getY())));
        current.setLocation(avgLoc);
        current.setDuration(prev.getDuration() / 2 + 1);
        prev.setDuration(prev.getDuration() / 2 + 1);

        waypoints.add(idx, current);
        initialize();
    }
}

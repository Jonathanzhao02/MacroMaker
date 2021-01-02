package main.java.impls.utils;

import main.java.impls.objects.Waypoint;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MacroUtils {
    public static List<Waypoint> preprocess(List<Waypoint> waypoints, double epsilon) {
        if (waypoints.isEmpty()) return waypoints;
        waypoints = MacroUtils.distinctTimestamps(waypoints); // Ensures no duplicate timestamps
        MacroUtils.assignDurations(waypoints, true); // Assigns durations to waypoints to label delays
        waypoints = MacroUtils.cleanWaypoints(waypoints, epsilon); // Cleans up macro to save space, represented by a few key points
        MacroUtils.assignDurations(waypoints, false); // Assign durations again to account for cleaned waypoints
        return waypoints;
    }

    public static int sortedAdd(List<Waypoint> waypoints, Waypoint waypoint) {
        List<Long> timestamps = waypoints.stream().map(Waypoint::getTimestamp).collect(Collectors.toList());
        int idx = Collections.binarySearch(timestamps, waypoint.getTimestamp());
        if (idx < 0) idx = ~idx;
        waypoints.add(idx, waypoint);
        return idx;
    }

    public static int[] sortedAddAll(List<Waypoint> waypoints, List<Waypoint> newPoints) {
        int[] idxs = new int[newPoints.size()];
        for (int i = 0; i < newPoints.size(); i++) {
            idxs[i] = sortedAdd(waypoints, newPoints.get(i));
        }
        return idxs;
    }

    public static List<Waypoint> dupeDelays(List<Waypoint> waypoints) {
        // Duplicate delays, so a waypoint exists at the beginning and end of each delay
        List<Waypoint> dupes = new LinkedList<>();
        waypoints.forEach(wp -> {
            if (wp.isDelayed()) {
                Waypoint dupe = new Waypoint();
                dupe.setLocation(wp.getLocation());
                dupe.setTimestamp(wp.getTimestamp() + wp.getDelay());
                dupe.setDuration(wp.getDuration());
                dupes.add(dupe);
            }
        });

        return dupes;
    }

    public static void assignDurations(List<Waypoint> waypoints, boolean assignDelayed) {
        // Recalculates all durations based on timestamps
        // ASSUMES SORTED
        long prevTimestamp = waypoints.get(waypoints.size() - 1).getTimestamp();
        int thresh = Waypoint.getDelayThreshold();
        assignDelayed = assignDelayed && thresh > 1;

        for (int i = waypoints.size() - 2; i >= 0; i--) {
            Waypoint current = waypoints.get(i);
            int duration = (int) (prevTimestamp - current.getTimestamp());

            if (assignDelayed && duration >= thresh) {
                int delay = duration - 2;
                duration = 2;
                current.setDelay(delay);
            }

            current.setDuration(duration);
            prevTimestamp = current.getTimestamp();
        }
    }

    public static void assignTimestamps(List<Waypoint> waypoints) {
        // Recalculates all timestamps based on durations
        // ASSUMES SORTED
        long sum = 0;

        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint current = waypoints.get(i);
            current.setTimestamp(sum);
            sum += current.getDuration();
        }
    }

    public static void normalizeTimestamps(List<Waypoint> waypoints) {
        // Normalizes all timestamps such that the minimum is zero
        long minTimestamp = waypoints.stream().mapToLong(Waypoint::getTimestamp).min().orElse(0);
        if (minTimestamp == 0) return;
        waypoints.forEach(waypoint -> waypoint.setTimestamp(waypoint.getTimestamp() - minTimestamp));
    }

    public static List<Waypoint> distinctTimestamps(List<Waypoint> waypoints) {
        // Removes all duplicate timestamps, keeping all waypoints with important actions
        Set<Object> timestamps = ConcurrentHashMap.newKeySet();

        List<Waypoint> keyPoints = waypoints
                .stream()
                .filter(waypoint -> !waypoint.isMouseOnly())
                .collect(Collectors.toList());
        timestamps.addAll(keyPoints.stream().map(Waypoint::getTimestamp).collect(Collectors.toList()));

        Predicate<Waypoint> filter = StreamUtils.distinctByKey(Waypoint::getTimestamp, timestamps);

        List<Waypoint> cleanedWaypoints = waypoints
                .stream()
                .filter(Waypoint::isMouseOnly)
                .filter(filter)
                .filter(StreamUtils.distinctByKey(Waypoint::getTimestamp))
                .collect(Collectors.toList());

        sortedAddAll(cleanedWaypoints, keyPoints);
        return cleanedWaypoints;
    }

    public static List<Waypoint> cleanWaypoints(List<Waypoint> waypoints, double epsilon) {
        // Uses RDP algorithm to simplify waypoints list
        List<Waypoint> keyPoints = waypoints.stream()
            .filter(waypoint -> !waypoint.isMouseOnly())
            .collect(Collectors.toList());

        List<Waypoint> mousePoints = new LinkedList<>(waypoints);
        mousePoints.removeAll(keyPoints);
        mousePoints.sort(Comparator.comparingLong(Waypoint::getTimestamp));

        List<Waypoint> simplifiedPoints = RamerDouglasPeucker(mousePoints, epsilon);
        sortedAddAll(simplifiedPoints, keyPoints);
        return simplifiedPoints;
    }

    public static double perpendicularDistance(Point origin, Point endpoint, Point midpoint) {
        Vector A = new Vector(endpoint.x - origin.x, endpoint.y - origin.y).normalize();
        Vector B = new Vector(midpoint.x - origin.x, midpoint.y - origin.y);
        A = A.mult(A.dot(B));
        return B.sub(A).sqrMag();
    }

    public static List<Waypoint> RamerDouglasPeucker(List<Waypoint> waypoints, double epsilon) {
        if (waypoints.size() < 2 || epsilon <= 0) return waypoints;

        double dmax = 0;
        int index = 0;
        int end = waypoints.size() - 1;

        Point origin = waypoints.get(0).getLocation();
        Point endpoint = waypoints.get(end).getLocation();

        for (int i = 1; i < end; i++) {
            Waypoint waypoint = waypoints.get(i);
            double d = perpendicularDistance(origin, endpoint, waypoint.getLocation());
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        List<Waypoint> out = new LinkedList<>();

        if (dmax > epsilon) {
            List<Waypoint> results1 = RamerDouglasPeucker(waypoints.subList(0, index + 1), epsilon);
            List<Waypoint> results2 = RamerDouglasPeucker(waypoints.subList(index, end + 1), epsilon);
            out.addAll(results1.subList(0, results1.size() - 1));
            out.addAll(results2);
        } else {
            out.add(waypoints.get(0));
            out.add(waypoints.get(end));
        }

        return out;
    }

    public static class Vector {
        public double x;
        public double y;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Vector mult(double scalar) {
            return new Vector(x * scalar, y * scalar);
        }

        public Vector sub(Vector b) {
            return new Vector(x - b.x, y - b.y);
        }

        public Vector add(Vector b) {
            return new Vector(x + b.x, y + b.y);
        }

        public double dot(Vector v) {
            return x * v.x + y * v.y;
        }

        public Vector normalize() {
            double mag = mag();
            if (mag > 0) {
                return new Vector(x / mag, y / mag);
            } else {
                return this;
            }
        }

        public double mag() {
            return Math.sqrt(sqrMag());
        }

        public double sqrMag() {
            return x * x + y * y;
        }
    }
}

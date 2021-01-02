package main.java.impls.objects;

import javafx.scene.paint.Color;

import java.util.*;

public class LinearColorGradient {

    private List<Double> ranges = new LinkedList<>();
    private List<Color> points = new LinkedList<>();

    public LinearColorGradient(Color start, Color end) {
        ranges.add(0.0);
        points.add(start);
        ranges.add(1.0);
        points.add(end);
    }

    public void addPoint(double v, Color c) {
        int idx = Collections.binarySearch(ranges, v);
        if (idx < 0) idx = ~idx;
        else return;
        ranges.add(idx, v);
        points.add(idx, c);
    }

    public Color getVal(double v) {
        v = Math.max(Math.min(v, 1), 0);
        int idx = 1;
        int prevIdx = 0;

        for (int i = 1; i < ranges.size(); i++) {
            if (ranges.get(i) < v) {
                idx = i + 1;
                prevIdx = i;
            }
        }

        double min = ranges.get(prevIdx);
        double max = ranges.get(idx);
        double range = max - min;
        v = (v - min) / range;

        Color start = points.get(prevIdx);
        Color end = points.get(idx);

        return Color.color(
                start.getRed() * (1 - v) + end.getRed() * v,
                start.getGreen() * (1 - v) + end.getGreen() * v,
                start.getBlue() * (1 - v) + end.getBlue() * v
        );
    }

}

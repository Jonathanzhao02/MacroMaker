package main.java.impls.enums;

public enum DoubleSetting {
    DELAY_THRESHOLD(
            100.0,
            "The amount of time before a waypoint is registered as a delay point.\nOnly applies during recording."
    ),
    DISPLAY_INTERVAL(
            1.0,
            "The size of interpolation steps (in ms) while displaying a macro."
    ),
    EPSILON(
            9.0,
            "The simplification factor of a macro. Higher = less waypoints, lower = more.\nOnly applies during recording."
    );

    private double val;
    private String desc;

    DoubleSetting(double val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public double getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}

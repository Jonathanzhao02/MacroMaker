package main.java.impls.enums;

public enum OptionSetting {
    SHOW_FRAME_ON_RECORD(
            true,
            "Whether the screen is covered while recording."
    ),
    LINEAR_INTERPOLATION(
            true,
            "Whether to use linear or cubic spline interpolation.\nLinear is suggested, as spline may overcompensate for waypoints close to one another."
    );

    private boolean val;
    private String desc;

    OptionSetting(boolean val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public boolean getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}

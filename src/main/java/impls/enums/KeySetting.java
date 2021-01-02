package main.java.impls.enums;

import org.jnativehook.keyboard.NativeKeyEvent;

public enum KeySetting {
    RECORD_KEY(
            NativeKeyEvent.VC_F,
            "Hotkey for recording a macro."
    ),
    QUIT_KEY(
            NativeKeyEvent.VC_BACKQUOTE,
            "Hotkey for quitting the program. Basically your emergency exit in case a macro goes rogue."
    );

    private int val;
    private String desc;

    KeySetting(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}

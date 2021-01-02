package main.java.impls.utils;

import org.jnativehook.mouse.NativeMouseEvent;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class NativeEnumMapper {
    public static final Map<Integer, Integer> nativeToJavaMouseMap = new HashMap<>();
    public static final Map<Integer, Integer> javaToNativeMouseMap = new HashMap<>();

    public static void initialize() {
        nativeToJavaMouseMap.put(NativeMouseEvent.BUTTON1, MouseEvent.BUTTON1);
        nativeToJavaMouseMap.put(NativeMouseEvent.BUTTON2, MouseEvent.BUTTON3);
        nativeToJavaMouseMap.put(NativeMouseEvent.BUTTON3, MouseEvent.BUTTON2);
        nativeToJavaMouseMap.put(NativeMouseEvent.BUTTON4, 4);
        nativeToJavaMouseMap.put(NativeMouseEvent.BUTTON5, 5);
    }

    public static int nativeToJavaMouse(int val) {
        return nativeToJavaMouseMap.get(val);
    }

}

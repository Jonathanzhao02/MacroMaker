package main.java.impls.objects.components;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RecordingCanvas extends DrawCanvas {
    private Map<Integer, Boolean> keysPressed = new HashMap<>();

    public RecordingCanvas() {
        super();
        setFocusTraversable(true);

        setOnMouseDragged(val -> {
            drawPixel((int) val.getX(), (int) val.getY(), Color.RED);
        });

        setOnMouseMoved(val -> {
            drawPixel((int) val.getX(), (int) val.getY(), Color.GREEN);
        });

        setOnKeyPressed(val -> {
            if (keysPressed.getOrDefault(val.getCode().ordinal(), false)) return;
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            drawText(val.getText(), (int) mousePos.getX(), (int) mousePos.getY(), Color.BLUE, Color.WHITE);
            keysPressed.put(val.getCode().ordinal(), true);
        });

        setOnKeyReleased(val -> {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            drawText(val.getText(), (int) mousePos.getX(), (int) mousePos.getY(), Color.YELLOW, Color.BLACK);
            keysPressed.put(val.getCode().ordinal(), false);
        });
    }
}

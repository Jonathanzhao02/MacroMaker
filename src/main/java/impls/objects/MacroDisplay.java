package main.java.impls.objects;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.*;
import main.java.impls.objects.components.DrawCanvas;
import main.java.impls.objects.components.WaypointWidget;
import main.java.impls.objects.events.SelectionEvent;
import main.java.impls.objects.hotkeys.MacroHotkey;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MacroDisplay {
    private static double displayInterval = 1;
    private Stage stage;
    private DrawCanvas canvas;
    private Group root;
    private MacroHotkey macro;

    public static void setDisplayInterval(double interval) {
        displayInterval = interval;
    }

    public MacroDisplay() {
        super();
        initialize();
        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (macro != null) {
                    macro.initialize();
                }

                stage.close();
                macro = null;
            }
        });
    }

    public void initialize() {
        root = new Group();
        canvas = new DrawCanvas();
        stage = new Stage();
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty());
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOpacity(0.4);
        stage.setScene(scene);
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    public void displayMacro(MacroHotkey macro) {
        this.macro = macro;
        root.getChildren().setAll(canvas);

        // TODO:
        // VISUAL EDITING
        // IMPROVE WAYPOINT DISPLAY
        // add table of waypoints, selection pushes selected waypoint to front
        // add border around selected waypoint

        LinearColorGradient gradient = new LinearColorGradient(Color.RED, Color.VIOLET);
        gradient.addPoint(1.0 / 6, Color.ORANGE);
        gradient.addPoint(2.0 / 6, Color.YELLOW);
        gradient.addPoint(3.0 / 6, Color.GREEN);
        gradient.addPoint(4.0 / 6, Color.BLUE);
        gradient.addPoint(5.0 / 6, Color.INDIGO);
        List<Waypoint> waypoints = macro.getWaypoints();

        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);

            WaypointWidget widget = new WaypointWidget(wp, i - 1, i + 1 < waypoints.size() ? i + 1 : -1, (int) wp.getX(), (int) wp.getY(), 3, gradient.getVal((double) i / waypoints.size()));
            root.getChildren().add(widget);
            widget.addEventHandler(SelectionEvent.SELECTION_EVENT, e -> {
                if (e.getObject().getClass() == Waypoint.class) {
                    Waypoint selected = (Waypoint) e.getObject();
                    macro.deleteWaypoint(selected);
                    root.getChildren().remove(widget);
                } else if (e.getObject().getClass() == Integer.class) {
                    int idx = (int) e.getObject();
                    macro.addEmptyWaypoint(idx);
                    displayMacro(macro);
                }
            });

            widget.addEventHandler(ActionEvent.ANY, e -> {
                macro.initialize();
                drawInterpolatedPoints();
            });
        };

        drawInterpolatedPoints();
        stage.show();
    }

    public void drawInterpolatedPoints() {
        canvas.clear();
        if (macro == null) return;
        List<Integer> xInterpolation = macro.getInterpolatedX(displayInterval)
                .stream()
                .map(Double::intValue)
                .collect(Collectors.toList());
        List<Integer> yInterpolation = macro.getInterpolatedY(displayInterval)
                .stream()
                .map(Double::intValue)
                .collect(Collectors.toList());

        List<Point> interpolatedPoints = IntStream.range(0, xInterpolation.size())
                .mapToObj(n -> new Point(xInterpolation.get(n), yInterpolation.get(n)))
                .collect(Collectors.toList());

        canvas.drawPoints(interpolatedPoints, Color.WHITE);
    }
}

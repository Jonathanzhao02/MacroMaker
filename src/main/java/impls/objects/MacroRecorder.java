package main.java.impls.objects;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.impls.objects.components.RecordingCanvas;
import main.java.impls.objects.nativelisteners.WaypointInputListener;
import org.jnativehook.GlobalScreen;

import java.util.Comparator;
import java.util.List;

public class MacroRecorder {
    private Stage recordingStage;
    private RecordingCanvas canvas;
    private WaypointInputListener waypointInputListener;

    public MacroRecorder() {
        super();
        recordingStage = constructRecordingStage();
        waypointInputListener = new WaypointInputListener();
    }

    public Stage constructRecordingStage() {
        Group root = new Group();
        canvas = new RecordingCanvas();
        root.getChildren().add(canvas);
        Stage stage = new Stage();
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty());
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOpacity(0.4);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        return stage;
    }

    public void activate(boolean showStage) {
        if (showStage) {
            canvas.clear();
            Platform.runLater(recordingStage::show);
        }

        waypointInputListener.reset();
        GlobalScreen.addNativeMouseListener(waypointInputListener);
        GlobalScreen.addNativeMouseMotionListener(waypointInputListener);
        GlobalScreen.addNativeKeyListener(waypointInputListener);
    }

    public List<Waypoint> deactivate() {
        if (recordingStage.isShowing()) {
            Platform.runLater(recordingStage::close);
        }

        GlobalScreen.removeNativeMouseListener(waypointInputListener);
        GlobalScreen.removeNativeMouseMotionListener(waypointInputListener);
        GlobalScreen.removeNativeKeyListener(waypointInputListener);

        List<Waypoint> waypoints = waypointInputListener.getWaypoints();

        if (waypoints.size() > 2) {
            waypoints.sort(Comparator.comparingLong(Waypoint::getTimestamp));
            waypoints = waypoints.subList(1, waypoints.size() - 2); // Remove recording hotkey waypoints
        } else {
            waypoints.clear(); // Ignore tiny macros
        }

        return waypoints;
    }
}
